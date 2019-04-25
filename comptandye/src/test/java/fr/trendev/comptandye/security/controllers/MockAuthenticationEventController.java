/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class MockAuthenticationEventController implements
        AuthenticationEventController {

    private static final Logger LOG = Logger.getLogger(
            MockAuthenticationEventController.class.getName());

    @Override
    public void logout(String email) {
        LOG.log(Level.INFO, "Mocking LOG-IN event for user {0}", email);
    }

    @Override
    public void login(String email) {
        LOG.log(Level.INFO, "Mocking LOG-OUT event for user {0}", email);
    }

}
