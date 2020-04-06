/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class AuthenticationEventControllerProducer {

    private AuthenticationEventController authenticationEventController;

    private static final Logger LOG = Logger.getLogger(
            AuthenticationEventControllerProducer.class.getName());

    @Produces
    @Default
    public AuthenticationEventController getSlackAuthenticationEventController() {
        if (this.authenticationEventController == null) {
            this.authenticationEventController = new SlackAuthenticationEventController();
        }
        return this.authenticationEventController;
    }

}
