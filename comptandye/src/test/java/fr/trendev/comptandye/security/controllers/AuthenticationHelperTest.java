/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.Optional;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class AuthenticationHelperTest {

    public AuthenticationHelperTest() {
    }

    @Test
    public void testIsPresentWithOptional() {
        String email = "email";
        assert Optional.of(email).isPresent();
    }

    @Test
    public void testIsPresentWithEmptyOptional() {
        Optional empty = Optional.empty();
        assert !empty.isPresent();
    }

    @Test
    public void testOptionalOfMethod() {
        try {
            boolean value = Optional.of(null).isPresent();
            assert value;
        } catch (NullPointerException ex) {
            assert ex != null;
        }
    }

    @Test
    public void testFilterMapWithOptional() {
        String email = "email";

        int l = Optional.of(email)
                .filter(s -> !s.isEmpty())
                .map(v -> v.length())
                .orElse(0);

        assert l == email.length();
        assert l != 0;
    }

    @Test
    public void testFilterWithEmptyString() {
        int l = Optional.of("")
                // shoud return an empty Optional
                .filter(s -> !s.isEmpty())
                // should not be used because Optional is empy
                .map(v -> v.length())
                // no value to get, so returns -1
                .orElse(-1);

        assert l == -1;
    }

    @Test
    public void testMapWithEmptyString() {
        int l = Optional.of("")
                .map(v -> v.length())
                .orElse(-1);

        assert l == 0;
    }

    @Test
    public void testMapWithEmptyOptional() {
        int l = Optional.<String>empty()
                .map(v -> v.length() + 1)
                .orElse(0);

        assert l == 0;
    }

}
