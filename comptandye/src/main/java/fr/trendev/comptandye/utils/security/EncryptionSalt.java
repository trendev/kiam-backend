/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.security;

/**
 *
 * @author jsie
 */
public enum EncryptionSalt {
    LABOR("Labor omnia vincit improbus."),
    PRAESTAT("Præstat cautela quam medela.");
    private final String value;

    private EncryptionSalt(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
