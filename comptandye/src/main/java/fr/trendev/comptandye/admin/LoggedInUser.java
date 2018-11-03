/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.admin;

import javax.servlet.http.HttpSession;

/**
 * Entry which will represent a logged-in user. Used to flap the tracker contain
 */
public class LoggedInUser {

    private String email;
    private String type;
    private HttpSession httpSession;

    public LoggedInUser() {
    }

    public LoggedInUser(String email, String type, HttpSession httpSession) {
        this.email = email;
        this.type = type;
        this.httpSession = httpSession;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public HttpSession getHttpSession() {
        return httpSession;
    }

}
