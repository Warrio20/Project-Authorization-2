package org.warrio.middleware;

import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.http.HttpStatus;
import io.javalin.validation.ValidationException;

import java.util.Map;

public class ErrorMiddleware {
    public static void HandleError(Exception error, Context ctx){
        if(error instanceof HttpResponseException){
            HttpResponseException e = (HttpResponseException) error;
            ctx.status(e.getStatus());
            ctx.json(new ErrorResponse(e.getStatus(),e.getMessage(), e.getDetails()));
        } else if (error instanceof ValidationException){
            ctx.status(HttpStatus.BAD_REQUEST);
            ctx.json(Map.of("message", "Validation error","status", HttpStatus.BAD_REQUEST.getCode() , "details", ((ValidationException) error).getErrors()));
        } else {
            throw new HttpResponseException(HttpStatus.INTERNAL_SERVER_ERROR.getCode(), "Unexpected error");
        }
    }
}
