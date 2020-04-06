/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.security.SecureRandom;
import java.util.Objects;
import java.util.Optional;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class PasswordManager {

    private final String sequence;
    private final SecureRandom random;
    private final int default_size;

    @Inject
    private Pbkdf2PasswordHash pbkdf2PasswordHash;

    public PasswordManager() {
        sequence = "azertyuiopmlkjhgfdsqwxcvbn0123456789AZERTYUIOPMLKJHGFDSQWXCVBN";
        random = new SecureRandom();
        default_size = 10;
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
                .map(pwd_ -> pbkdf2PasswordHash.generate(pwd_.toCharArray()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Password or String to encrypt cannot be null or empty"));
    }

}
