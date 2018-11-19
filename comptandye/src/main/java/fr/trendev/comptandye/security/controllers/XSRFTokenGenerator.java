/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class XSRFTokenGenerator {

    /**
     * Generates a XSRF token
     *
     * @return the generated token
     */
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
