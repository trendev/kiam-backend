/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Controls the logins/logout directly in the active session tracker.
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
    public void login(String email) {
        this.getBeanManager()
                .getEvent()
                .select(new LoginDetectedLiteral())
                .fire(this.buildText(email, "CONNECTED"));
    }

    @Override
    public void logout(String email) {
        this.getBeanManager()
                .getEvent()
                .select(new LoginDetectedLiteral())
                .fire(this.buildText(email, "EXITED"));
    }

    private JsonObject buildText(String email, String status) {
        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("text",
                        "User `"
                        + email + "` : *" + status + "*");

        if ("CONNECTED".equals(status)) {
            builder.add("color", "#36a64f");
        } else {
            builder.add("color", "#fbb040");
        }

        return builder.build();
    }

}
