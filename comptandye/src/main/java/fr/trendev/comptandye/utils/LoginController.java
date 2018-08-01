/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.utils.observers.qualifiers.LoginDetected;
import fr.trendev.comptandye.utils.observers.qualifiers.LogoutDetected;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Controls the logins/logout directly in the active session tracker.
 *
 * @author jsie
 */
@Singleton
@Startup
public class LoginController {

    @Inject
    @LoginDetected
    private Event<JsonObject> loginEvent;

    @Inject
    @LogoutDetected
    private Event<JsonObject> logoutEvent;

    public void login(String email) {
        loginEvent.fire(this.buildText(email, "CONNECTED"));
    }

    public void logout(String email) {
        logoutEvent.fire(this.buildText(email, "EXITED"));
    }

    private JsonObject buildText(String email, String status) {
        return Json.createObjectBuilder()
                .add("text",
                        "User `"
                        + email + "` : *" + status + "*")
                //                .add("text", "*" + status + "*")
                .build();
    }

}
