/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import java.util.Optional;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(Optional.of(email).isPresent());
    }

    @Test
    public void testIsPresentWithEmptyOptional() {
        Optional empty = Optional.empty();
        assertFalse(empty.isPresent());
    }

    @Test
    public void testOptionalOfMethod() {
        assertThrows(NullPointerException.class, () -> {
            boolean value = Optional.of(null).isPresent();
            assertTrue(value);
        });
    }

    @Test
    public void testFilterMapWithOptional() {
        String email = "email";

        int l = Optional.of(email)
                .filter(s -> !s.isEmpty())
                .map(v -> v.length())
                .orElse(0);

        assertEquals(l, email.length());
        assertNotEquals(l, 0);
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

        assertEquals(l, -1);
    }

    @Test
    public void testMapWithEmptyString() {
        int l = Optional.of("")
                .map(v -> v.length())
                .orElse(-1);

        assertEquals(l, 0);
    }

    @Test
    public void testMapWithEmptyOptional() {
        int l = Optional.<String>empty()
                .map(v -> v.length() + 1)
                .orElse(0);
        assertEquals(l, 0);
    }

}
