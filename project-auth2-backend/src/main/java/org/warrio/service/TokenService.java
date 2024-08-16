package org.warrio.service;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Updates;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.bson.Document;
import org.warrio.App;
import org.warrio.DTOs.TokenSet;
import org.warrio.DTOs.UserDTO;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

import static com.mongodb.client.model.Filters.eq;

public class TokenService {
    public static TokenSet generateToken(Object payload){
        String secretAccess = App.dotenv.get("JWT_ACCESS_SECRET");
        String secretRefresh = App.dotenv.get("JWT_REFRESH_SECRET");
        SecretKey accessKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretAccess));
        SecretKey refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretRefresh));
        String accessToken = Jwts.builder().expiration(new Date(new Date().getTime() + 60 * 60 * 1000)).claim("userInfo", payload).signWith(accessKey).compact();
        String refreshToken = Jwts.builder().expiration(new Date(new Date().getTime() + 15 * 24 * 60 * 60 * 1000)).claim("userInfo", payload).signWith(refreshKey).compact();
        return new TokenSet(accessToken,refreshToken);
    }
    public static void saveToken(String username, String refreshToken){
        MongoCollection<Document> collection = App.db.getCollection("tokens");
        if(collection.find(eq("username", username)).first() != null){
            collection.updateOne(eq("username",username), Updates.set("refreshToken",refreshToken));
        } else {
            collection.insertOne(new Document().append("username",username).append("refreshToken",refreshToken));
        }
    }
    public static String removeToken(String refreshToken){
        MongoCollection<Document> collection = App.db.getCollection("tokens");
        collection.deleteOne(eq("refreshToken", refreshToken));
        return refreshToken;
    }
    public static UserDTO validateAccessToken(String token){
        try {
            String secretAccess = App.dotenv.get("JWT_ACCESS_SECRET");
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretAccess));
            Map userInfo = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userInfo", Map.class);
            return new UserDTO((String) userInfo.get("email"), (String) userInfo.get("username"), (boolean) userInfo.get("isActivated"));
        } catch (Exception e){
            return null;
        }
    }
    public static UserDTO validateRefreshToken(String token){
        try {
            String secretRefresh = App.dotenv.get("JWT_REFRESH_SECRET");
            SecretKey secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretRefresh));
            Map userInfo = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("userInfo", Map.class);
            return new UserDTO((String) userInfo.get("email"), (String) userInfo.get("username"), (boolean) userInfo.get("isActivated"));
        } catch (Exception e){
            return null;
        }
    }
    public static Document findToken(String refreshToken){
        MongoCollection<Document> collection = App.db.getCollection("tokens");
        return collection.find(eq("refreshToken", refreshToken)).first();
    }
}
