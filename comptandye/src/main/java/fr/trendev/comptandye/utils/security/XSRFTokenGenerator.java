/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.security;

import fr.trendev.comptandye.utils.security.PasswordGenerator;
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
    PasswordGenerator passwordGenerator;

    /**
     * Generates a XSRF token
     *
     * @return the generated token
     */
    public String generate() {
        return passwordGenerator.autoGenerate(128);
    }

}
