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
    public void emitLogoutEvent(String email) {
        LOG.log(Level.INFO, "Mocking LOG-IN event for user {0}", email);
    }

    @Override
    public void emitLoginEvent(String email) {
        LOG.log(Level.INFO, "Mocking LOG-OUT event for user {0}", email);
    }

    @Override
    public void emitFirestoreIssue(String message, String details) {
        LOG.log(Level.INFO,
                "Mocking FIRESTORE-ISSUE event with message :\n{0} and details :\n{1}",
                new Object[]{message,
                    details});
    }

    @Override
    public void emitJWTForgeryDetectedEvent(String token) {
        LOG.log(Level.SEVERE, "### JWT FORGERY DETECTED ###\n{0}", token);
    }

}
