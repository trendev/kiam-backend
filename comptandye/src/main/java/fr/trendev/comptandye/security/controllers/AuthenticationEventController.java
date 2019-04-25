/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Controls the logins/logout directly in the active session tracker.
 *
 * @author jsie
 */
@Stateless
public class AuthenticationEventController {

    private static final Logger LOG = Logger.getLogger(
            AuthenticationEventController.class.getName());

//    @Inject
//    @LoginDetected
//    private Event<JsonObject> loginEvent;
//    @Inject
//    @LogoutDetected
//    private Event<JsonObject> logoutEvent;
    private BeanManager getBeanManager() {
        LOG.severe("Getting a Bean Manager");
        return CDI.current().getBeanManager();
    }

    public void login(String email) {
        LOG.severe("#### login detected ####");
        this.getBeanManager()
                .getEvent()
                .select(JsonObject.class, new LoginDetectedLiteral())
                .fire(this.buildText(email, "CONNECTED"));
    }

    public void logout(String email) {
//        logoutEvent.fire(this.buildText(email, "EXITED"));
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
