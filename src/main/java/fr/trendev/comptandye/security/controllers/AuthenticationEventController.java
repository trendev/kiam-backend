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

    public void login(String email);

    public void logout(String email);

    public void postFirestoreIssue(String message, String details);
}
