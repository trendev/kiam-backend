/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.io.Serializable;

/**
 *
 * @author jsie
 */
public interface AuthenticationEventController extends Serializable {

    public void emitLoginEvent(String email);

    public void emitLogoutEvent(String email);

    public void emitFirestoreIssue(String message, String details);

    public void emitJWTForgeryDetectedEvent(String token);
}
