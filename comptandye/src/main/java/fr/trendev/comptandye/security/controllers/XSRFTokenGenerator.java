/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class XSRFTokenGenerator {

    @Inject
    PasswordManager passwordManager;

    /**
     * Generates a XSRF token
     *
     * @return the generated token
     */
    public String generate() {
        return passwordManager.autoGenerate(128);
    }

}
