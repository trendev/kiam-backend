/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans.xsrf;

import fr.trendev.comptandye.utils.PasswordGenerator;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class XSRFTokenGenerator {

    /**
     * Generates a token and add it as an attribute inside the session
     *
     * @param session the session which will be set with the XSRF-TOKEN Cookie
     * @return the generated token
     */
    public String generate(HttpSession session) {
        String xsrfToken = PasswordGenerator.autoGenerate(128);
        session.setAttribute("XSRF-TOKEN", xsrfToken);
        return xsrfToken;
    }

}
