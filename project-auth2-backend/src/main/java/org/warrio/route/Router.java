package org.warrio.route;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import org.warrio.controllers.UserController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Router {
    public static Javalin Route(){
        return Javalin.create(config -> {
            config.bundledPlugins.enableCors(cors -> cors.addRule((corsRule -> corsRule.reflectClientOrigin = true)));
            config.router.apiBuilder(() -> {
                path("/api", () -> {
                    UserController userController = new UserController();
                    post("/registration", userController::registration);
                    post("/login", userController::login);
                    post("/logout", userController::logout);
                    post("/forgot-password", userController::forgotPassword);
                    post("/reset-password", userController::resetPassword);
                    get("/activate/{link}", userController::activate);
                    get("/refresh", userController::refresh);
                    get("/users", userController::getUsers);
                });
            });
        });
    }
}
