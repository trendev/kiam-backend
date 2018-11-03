/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.security.SecureRandom;
import java.util.logging.Logger;
import javax.ejb.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class PasswordGenerator {

    private final String sequence;
    private final SecureRandom rand;
    private final int default_size;
    private static final Logger LOG = Logger.getLogger(PasswordGenerator.class.
            getName());
    private final HashingMechanism hashingMechanism;

    public PasswordGenerator() {
        sequence = "azertyuiopmlkjhgfdsqwxcvbn0123456789._-!?@AZERTYUIOPMLKJHGFDSQWXCVBN";
        rand = new SecureRandom();
        default_size = 10;
        hashingMechanism = new HashingMechanism();
    }

    public String autoGenerate(int s) {
        String pwd = "";
        int size = (s <= 0 || s > 4096) ? default_size : s;

        for (int i = 0; i < size; i++) {
            pwd += sequence.charAt(rand.nextInt(sequence.length()));
        }
        return pwd;
    }

    public String autoGenerate() {
        return autoGenerate(default_size);
    }

    public String encrypt_SHA256(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password or String to encrypt cannot be null or empty");
        }
        return hashingMechanism.hash_SHA256_base64(pwd);
    }

    public String encrypt_SHA256(String pwd, String base) {
        if (pwd == null || pwd.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password to encrypt cannot be null or empty");
        }
        switch (base) {
            case "base64":
                return hashingMechanism.hash_SHA256_base64(pwd);
            case "base16":
                return hashingMechanism.hash_SHA256_base16(pwd);
            default:
                return hashingMechanism.hash_SHA256_base64(pwd);
        }

    }

}
