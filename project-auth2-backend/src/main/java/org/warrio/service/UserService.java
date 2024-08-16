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
import org.warrio.DTOs.AccountResult;
import org.warrio.DTOs.TokenSet;
import org.warrio.DTOs.UserDTO;
import org.warrio.DTOs.UserProfile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class UserService {
    private final MongoCollection<Document> userCollection;
    public UserService(){
        this.userCollection = App.db.getCollection("users");
    }
    public AccountResult registration(String username, String email, String password) throws HttpResponseException {
        Document emailUser = userCollection.find(eq("email", email)).first();
        Document nameUser = userCollection.find(eq("username", username)).first();
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
        MailService mailService = new MailService();
        mailService.sendActivation(email, App.dotenv.get("API_URL") + "/api/activate/" + activationLink);
        UserDTO userDTO = new UserDTO(user);
        TokenSet tokenSet = TokenService.generateToken(userDTO);
        TokenService.saveToken(userDTO.username, tokenSet.refreshToken);
        return new AccountResult(tokenSet.accessToken, tokenSet.refreshToken, userDTO);
    };
    public void activate(String activationLink) {
        if(userCollection.find(eq("activationLink", activationLink)).first() == null){
            throw new BadRequestResponse("Incorrect link");
        };
        userCollection.updateOne(eq("activationLink",activationLink), Updates.set("isActivated", true));
    };

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
        UserDTO userDTO = new UserDTO(user);
        TokenSet tokenSet = TokenService.generateToken(userDTO);
        TokenService.saveToken(userDTO.username,tokenSet.refreshToken);
        return new AccountResult(tokenSet.accessToken, tokenSet.refreshToken, userDTO);
    }
    public List<UserProfile> getAllUsers(){
        FindIterable<Document> users = userCollection.find();
        List<UserProfile> documents = new ArrayList<>();
        users.forEach(document -> {
            documents.add(new UserProfile(document.getString("username"), document.getBoolean("isActivated")));
        });
        return documents;
    }
    public AccountResult changeUsername(String newUsername,String username){
        Document account = userCollection.find(eq("username", username)).first();
        if(account == null) throw new UnauthorizedResponse();
        if(new Date().getTime() - account.getLong("usernameChangeDate") < (5*24*60*60*1000)) throw new BadRequestResponse("It hasn't been 5 days since the last username change");
        account.put("username",newUsername);
        return changeAccount(account, username);
    }
    public AccountResult changePassword(String password, String username){
        Document account = userCollection.find(eq("username", username)).first();
        if(account == null) throw new UnauthorizedResponse();
        String hashPassword = BCrypt.withDefaults().hashToString(11,password.toCharArray());
        account.put("password", hashPassword);
        return changeAccount(account,username);
    }
    public AccountResult changeAccount(Document newAccount, String username){
        UserDTO userDTO = new UserDTO(newAccount);
        TokenSet tokenSet = TokenService.generateToken(userDTO);
        TokenService.saveToken(username,tokenSet.refreshToken);
       userCollection.updateOne(eq("username",username), newAccount);
       return new AccountResult(tokenSet.accessToken,tokenSet.refreshToken,userDTO);
    }
}
