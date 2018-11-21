/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

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
    private Pbkdf2PasswordHash hashingMechanism;

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
    public PasswordManager(Pbkdf2PasswordHash hashingMechanism) {
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

    public String hashPassword(String pwd) {
        return Optional.ofNullable(pwd)
                .filter(Objects::nonNull)
                .filter(pwd_ -> !pwd_.isEmpty())
                .map(pwd_ -> hashingMechanism.generate(pwd_.toCharArray()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Password or String to encrypt cannot be null or empty"));
    }

}
