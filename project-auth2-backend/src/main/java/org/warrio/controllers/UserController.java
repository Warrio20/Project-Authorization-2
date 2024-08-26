package org.warrio.controllers;

import io.javalin.http.Context;
import io.javalin.http.Cookie;
import io.javalin.http.HttpStatus;
import org.warrio.App;
import org.warrio.DTOs.Body.ForgotPasswordBody;
import org.warrio.DTOs.Body.ResetPasswordBody;
import org.warrio.middleware.AuthMiddleware;
import org.warrio.DTOs.Results.AccountResult;
import org.warrio.DTOs.Body.LoginBody;
import org.warrio.DTOs.Body.RegistrationBody;
import org.warrio.validation.Validation;
import org.warrio.middleware.ErrorMiddleware;
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

            userService.registration(username,email,password);
            ctx.status(HttpStatus.ACCEPTED);
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        }

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
            AuthMiddleware.CheckAuth(ctx);
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
    public void forgotPassword(Context ctx){
        try {
            ForgotPasswordBody body = Validation.ForgotPasswordValidation(ctx);
            userService.forgotPassword(body.email);
            ctx.status(HttpStatus.ACCEPTED);
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        }
    }
    public void resetPassword(Context ctx){
        try {
            ResetPasswordBody body = Validation.ResetPasswordValidation(ctx);
            userService.resetPassword(body.newPassword, body.uuid);
            ctx.status(HttpStatus.ACCEPTED);
        } catch (Exception e){
            ErrorMiddleware.HandleError(e, ctx);
        }
    }
}
