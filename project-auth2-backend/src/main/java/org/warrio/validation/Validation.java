package org.warrio.validation;

import io.javalin.http.Context;
import io.javalin.validation.ValidationException;
import org.warrio.DTOs.Body.ForgotPasswordBody;
import org.warrio.DTOs.Body.LoginBody;
import org.warrio.DTOs.Body.RegistrationBody;
import org.warrio.DTOs.Body.ResetPasswordBody;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validation {
    public static RegistrationBody registrationBodyValidation(Context ctx){
        return ctx.bodyValidator(RegistrationBody.class)
                .check((RegistrationBody body) -> {
                    if(body.email != null){
                        try {
                            InternetAddress emailAddr = new InternetAddress(body.email);
                            emailAddr.validate();
                            return true;
                        } catch (AddressException ignored){
                            return false;
                        }
                    };
                    return false;
                }, "Invalid email")
                .check((RegistrationBody body) -> body.password != null && body.password.length() > 3 && body.password.length() < 30, "Invalid password")
                .check((RegistrationBody body) -> body.username != null && body.username.length() > 3, "Invalid username")
                .getOrThrow(ValidationException::new);
    }
    public static LoginBody loginBodyValidation(Context ctx){
        return ctx.bodyValidator(LoginBody.class)
                .check((LoginBody body) -> body.username != null, "Invalid username")
                .check((LoginBody body) -> body.password != null, "Invalid password")
                .getOrThrow(ValidationException::new);
    }
    public static ForgotPasswordBody ForgotPasswordValidation(Context ctx){
        return ctx.bodyValidator(ForgotPasswordBody.class)
                .check((ForgotPasswordBody body) -> body.email != null, "Invalid email")
                .getOrThrow(ValidationException::new);
    }
    public static ResetPasswordBody ResetPasswordValidation(Context ctx){
        return ctx.bodyValidator(ResetPasswordBody.class)
                .check((ResetPasswordBody body) -> body.newPassword != null && body.newPassword.length() > 3 && body.newPassword.length() < 30, "Invalid new password")
                .check((ResetPasswordBody body) -> body.uuid != null && !body.uuid.isEmpty(), "Invalid token")
                .getOrThrow(ValidationException::new);
    }
}
