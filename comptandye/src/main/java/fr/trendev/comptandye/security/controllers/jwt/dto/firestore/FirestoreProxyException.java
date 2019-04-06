/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

/**
 *
 * @author jsie
 */
public class FirestoreProxyException extends Exception {

    /**
     * Creates a new instance of <code>FirestoreProxyException</code> without
     * detail message.
     */
    public FirestoreProxyException() {
    }

    /**
     * Constructs an instance of <code>FirestoreProxyException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FirestoreProxyException(String msg) {
        super(msg);
    }
}
