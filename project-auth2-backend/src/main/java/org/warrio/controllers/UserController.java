package org.warrio.controllers;

import io.javalin.http.Context;
import io.javalin.http.Cookie;
import org.warrio.App;
import org.warrio.middleware.AuthMiddleware;
import org.warrio.DTOs.AccountResult;
import org.warrio.DTOs.LoginBody;
import org.warrio.DTOs.RegistrationBody;
import org.warrio.validation.Validation;
import org.warrio.middleware.ErrorMiddleware;
import org.warrio.DTOs.UserDTO;
import org.warrio.service.UserService;

import java.util.Date;


public class UserController {
    UserService userService = new UserService();
    public void registration(Context ctx){
        try{
            RegistrationBody bodyInfo = Validation.registrationBodyValidation(ctx);
            String email = bodyInfo.email;
            String password = bodyInfo.password;
            String username = bodyInfo.username;


            AccountResult registrationResult = userService.registration(username,email,password);

            setCookies(ctx, registrationResult);
            ctx.json(registrationResult);
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        };

    }
    public void login(Context ctx){
        try {
            LoginBody body = Validation.loginBodyValidation(ctx);
            String username = body.username;
            String password = body.password;
            AccountResult loginResult = userService.login(username,password);
            setCookies(ctx, loginResult);
            ctx.json(loginResult);
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        }
    }
    public void logout(Context ctx){
        try {
            String refreshToken = ctx.cookie("refreshToken");
            String token = userService.logout(refreshToken);
            ctx.removeCookie("refreshToken");
            ctx.json(token);
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        }
    }
    public void getUsers(Context ctx){
        try {
            UserDTO result = AuthMiddleware.CheckAuth(ctx, true);
            ctx.json(userService.getAllUsers());
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        }
    }
    public void refresh(Context ctx){
        try {
            String refreshToken = ctx.cookie("refreshToken");
            AccountResult refreshResult = userService.refresh(refreshToken);
            setCookies(ctx, refreshResult);
            ctx.json(refreshResult);
        } catch (Exception e){
            ErrorMiddleware.HandleError(e,ctx);
        }
    }

    public void setCookies(Context ctx, AccountResult result) {
        Cookie cookie = new Cookie("refreshToken", result.refreshToken);
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) (new Date().getTime() + 15 * 24 * 60 * 60 * 1000));
        ctx.cookie(cookie);
    }

    public void activate(Context ctx) {
        try {
            String activationLink = ctx.pathParam("link");
            userService.activate(activationLink);
            ctx.redirect(App.dotenv.get("CLIENT_URL"));
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        }
    }
}
