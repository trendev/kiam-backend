/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans.xsrf;

import fr.trendev.comptandye.utils.PasswordGenerator;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class XSRFTokenGenerator {

    /**
     * Generates a XSRF token
     *
     * @return the generated token
     */
    public String generate() {
        return PasswordGenerator.autoGenerate(128);
    }

}
