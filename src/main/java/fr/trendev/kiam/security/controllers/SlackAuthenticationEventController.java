/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import fr.trendev.kiam.security.controllers.qualifiers.FirestoreIssueLiteral;
import fr.trendev.kiam.security.controllers.qualifiers.JWTForgeryDetectedLiteral;
import fr.trendev.kiam.security.controllers.qualifiers.LoginDetectedLiteral;
import fr.trendev.kiam.security.controllers.qualifiers.LogoutDetectedLiteral;
import java.util.concurrent.CompletableFuture;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Controls the logins/emitLogoutEvent directly in the active session tracker.
 *
 * @author jsie
 */
public class SlackAuthenticationEventController implements
        AuthenticationEventController {

    public SlackAuthenticationEventController() {
    }

    private BeanManager getBeanManager() {
        return CDI.current().getBeanManager();
    }

    @Override
    public void emitLoginEvent(String email) {
        CompletableFuture.runAsync(() -> this.getBeanManager()
                .getEvent()
                .select(new LoginDetectedLiteral())
                .fire(this.buildText(email, "LOGIN")));
    }

    @Override
    public void emitLogoutEvent(String email) {
        CompletableFuture.runAsync(() -> this.getBeanManager()
                .getEvent()
                .select(new LogoutDetectedLiteral())
                .fire(this.buildText(email, "LOGOUT")));
    }

    private JsonObject buildText(String email, String status) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("text",
                        "User `"
                        + email + "` : *" + status + "*");

        if ("LOGIN".equals(status)) {
            builder.add("color", "#36a64f");
        } else {
            builder.add("color", "#fbb040");
        }

        return builder.build();
    }

    @Override
    public void emitFirestoreIssue(String message, String details) {
        CompletableFuture.runAsync(() -> {
            JsonObjectBuilder builder = Json.createObjectBuilder()
                    .add("text", message)
                    .add("footer", details)
                    .add("color", "#B33A3A");

            this.getBeanManager()
                    .getEvent()
                    .select(new FirestoreIssueLiteral())
                    .fire(builder.build());
        });
    }

    @Override
    public void emitJWTForgeryDetectedEvent(String token) {
        CompletableFuture.runAsync(() -> {
            JsonObjectBuilder builder = Json.createObjectBuilder()
                    .add("pretext", "*JWT FORGERY DETECTED*")
                    .add("text", token)
                    .add("footer", "Please, check the server logs")
                    .add("color", "#FF0000");

            this.getBeanManager()
                    .getEvent()
                    .select(new JWTForgeryDetectedLiteral())
                    .fire(builder.build());
        });
    }
}
