/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.security.SecureRandom;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author jsie
 */
@Singleton
public class PasswordManager {

    private final String sequence;
    private final SecureRandom random;
    private final int default_size;

    @Inject
    private HashingMechanism hashingMechanism;

    public PasswordManager() {
        sequence = "azertyuiopmlkjhgfdsqwxcvbn0123456789._-!?@AZERTYUIOPMLKJHGFDSQWXCVBN";
        random = new SecureRandom();
        default_size = 10;
    }

    /**
     * Should be used for test purposes only
     *
     * @param hashingMechanism
     */
    public PasswordManager(HashingMechanism hashingMechanism) {
        this();
        this.hashingMechanism = hashingMechanism;
    }

    public String autoGenerate(int s) {
        String pwd = "";
        int size = (s <= 0 || s > 4096) ? default_size : s;

        for (int i = 0; i < size; i++) {
            pwd += sequence.charAt(random.nextInt(sequence.length()));
        }
        return pwd;
    }

    public String autoGenerate() {
        return autoGenerate(default_size);
    }

    // TODO : use Optional and use the new java ee 8 supported algorithm
    public String hashPassword(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password or String to encrypt cannot be null or empty");
        }
        return hashingMechanism.hash_SHA256_base64(pwd);
    }

}
