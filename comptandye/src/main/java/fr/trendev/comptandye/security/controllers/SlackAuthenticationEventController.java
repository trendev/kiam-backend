/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.CDI;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * Controls the logins/logout directly in the active session tracker.
 *
 * @author jsie
 */
@Singleton
public class SlackAuthenticationEventController implements
        AuthenticationEventController {

    private static final Logger LOG = Logger.getLogger(
            SlackAuthenticationEventController.class.getName());

    public SlackAuthenticationEventController() {
        LOG.log(Level.SEVERE, "Constructing {0}",
                SlackAuthenticationEventController.class.getName());
    }

    private BeanManager getBeanManager() {
        LOG.severe("Getting a Bean Manager");
        return CDI.current().getBeanManager();
    }

    @Override
    public void login(String email) {
        LOG.severe("#### login detected ####");
        this.getBeanManager()
                .getEvent()
                .select(new LoginDetectedLiteral())
                .fire(this.buildText(email, "CONNECTED"));
    }

    @Override
    public void logout(String email) {
        LOG.severe("#### logout detected ####");
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
