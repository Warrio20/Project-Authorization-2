package org.warrio.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.HttpResponseException;
import io.javalin.http.UnauthorizedResponse;
import org.bson.Document;
import org.warrio.App;
import org.warrio.DTOs.Results.AccountResult;
import org.warrio.DTOs.Results.TokenSet;
import org.warrio.DTOs.UserDTO;
import org.warrio.DTOs.UserProfile;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class UserService {
    private final MongoCollection<Document> userCollection;
    public UserService(){
        this.userCollection = App.db.getCollection("users");
    }
    public void registration(String username, String email, String password) throws HttpResponseException {
        email = email.toLowerCase();
        Document emailUser = userCollection.find(eq("email", email)).first();
        Document nameUser = userCollection.find(regex("username", "^" + username + "$", "i")).first();
        if(emailUser != null){
            throw new BadRequestResponse("User with this email address already exists");
        } else if(nameUser != null){
            throw new BadRequestResponse("User with this username already exists");
        }
        String hashPassword = BCrypt.withDefaults().hashToString(11,password.toCharArray());
        Document user = new Document();
        UUID activationLink = UUID.randomUUID();
        user.append("username", username);
        user.append("email", email);
        user.append("password", hashPassword);
        user.append("activationLink", activationLink.toString());
        user.append("isActivated", false);
        userCollection.insertOne(user);
        MailService.sendActivation(email, App.dotenv.get("API_URL") + "/api/activate/" + activationLink);
        UserDTO userDTO = new UserDTO(user);
        TokenSet tokenSet = TokenService.generateToken(userDTO);
        TokenService.saveToken(userDTO.username, tokenSet.refreshToken);
    }
    public void activate(String activationLink) {
        if(userCollection.find(eq("activationLink", activationLink)).first() == null){
            throw new BadRequestResponse("Incorrect link");
        }
        userCollection.updateOne(eq("activationLink",activationLink), Updates.set("isActivated", true));
    }

    public AccountResult login(String username, String password) {
        Document user = userCollection.find(eq("username", username)).first();
        if(user == null){
            throw new BadRequestResponse("User with this username doesn't exist");
        }
        String userPassword = user.getString("password");
        boolean result = BCrypt.verifyer().verify(password.toCharArray(), userPassword.toCharArray()).verified;
        if(!result){
            throw new BadRequestResponse("Incorrect username or password");
        }
        if(!user.getBoolean("isActivated")){
            throw new UnauthorizedResponse("You didn't activate your account, please check your email to activate it");
        }
        UserDTO userDTO = new UserDTO(user);
        TokenSet tokenSet = TokenService.generateToken(userDTO);
        TokenService.saveToken(userDTO.username,tokenSet.refreshToken);
        return new AccountResult(tokenSet.accessToken, tokenSet.refreshToken, userDTO);
    }
    public String logout(String refreshToken){
        return TokenService.removeToken(refreshToken);
    }
    public AccountResult refresh(String refreshToken){
        if(refreshToken == null){
            throw new UnauthorizedResponse();
        }
        UserDTO decodedPayload = TokenService.validateRefreshToken(refreshToken);
        Document dbUser = TokenService.findToken(refreshToken);
        if(dbUser == null || decodedPayload == null){
            throw new UnauthorizedResponse();
        }
        Document user = userCollection.find(eq("username", dbUser.getString("username"))).first();
        if(!user.getBoolean("isActivated")){
            throw  new UnauthorizedResponse();
        }
        UserDTO userDTO = new UserDTO(user);
        TokenSet tokenSet = TokenService.generateToken(userDTO);
        TokenService.saveToken(userDTO.username,tokenSet.refreshToken);
        return new AccountResult(tokenSet.accessToken, tokenSet.refreshToken, userDTO);
    }
    public List<UserProfile> getAllUsers(){
        FindIterable<Document> users = userCollection.find();
        List<UserProfile> documents = new ArrayList<>();
        users.forEach(document -> documents.add(new UserProfile(document.getString("username"), document.getBoolean("isActivated"))));
        return documents;
    }
    public void forgotPassword(String email){
        Document user = userCollection.find(eq("email", email)).first();
        if(user == null){
            throw new UnauthorizedResponse("There is no user with this email");
        }
        UUID uuidLink = UUID.randomUUID();
        String link = App.dotenv.get("CLIENT_URL")+"/change-password?token=" + uuidLink;
        userCollection.updateOne(user, Updates.set("changePassword", uuidLink.toString()));
        MailService.sendPasswordReset(email,link);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                userCollection.updateOne(eq("email", email), eq("changePassword", null));
            }
        }, 2*60*60*1000);
    }
    public void resetPassword(String password, String uuid){
        Document account = userCollection.find(eq("changePassword", uuid)).first();
        if(account == null) throw new UnauthorizedResponse("Your token is outdated or have been already used");
        String hashPassword = BCrypt.withDefaults().hashToString(11,password.toCharArray());
        userCollection.updateOne(
                eq("_id", account.getObjectId("_id")),
                Updates.combine(
                    Updates.set("changePassword", null),
                    Updates.set("password", hashPassword)
                )
        );
    }
}
