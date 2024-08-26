package org.warrio.middleware;


import io.javalin.http.Context;
import io.javalin.http.UnauthorizedResponse;
import org.warrio.DTOs.UserDTO;
import org.warrio.service.TokenService;


public class AuthMiddleware {
    public static UserDTO CheckAuth(Context ctx){
            String auth = ctx.header("Authorization");
            if(auth == null){
                throw new UnauthorizedResponse();
            }
            String accessToken = auth.split(" ")[1];
            if(accessToken == null){
                throw new UnauthorizedResponse();
            };
            UserDTO decodedJWT = TokenService.validateAccessToken(accessToken);
            if(decodedJWT == null){
                throw new UnauthorizedResponse();
            }
        return decodedJWT;
    };
}
