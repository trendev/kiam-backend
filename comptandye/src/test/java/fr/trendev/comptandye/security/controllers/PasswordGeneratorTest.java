/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.stream.IntStream;
import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author jsie
 */
public class PasswordGeneratorTest {

    private PasswordManager passwordManager;
    private final Pbkdf2PasswordHashImpl pbkdf2PasswordHashImpl = new Pbkdf2PasswordHashImpl();

    public PasswordGeneratorTest() {
    }

    @Before
    public void init() {
        this.passwordManager = new PasswordManager(pbkdf2PasswordHashImpl);
    }

    @Test
    public void testConstructors() {
        PasswordManager passwordManager = new PasswordManager();
        // Will throw an Exception if no Pbkdf2PasswordHash implementation is injected
        Assertions.assertThrows(NullPointerException.class,
                () -> passwordManager.hashPassword("PASSWORD"));

    }

    @Test
    public void testAutoGenerate() {
        System.out.println("autoGenerate");

        int n = 5;
        int size = 12;

        long count = IntStream.range(0, n)
                .mapToObj(i -> passwordManager.hashPassword(passwordManager.
                        autoGenerate(size)))
                //                .peek(System.out::println)
                .count();

        assertFalse(count == 0);
        assertEquals(n, count, "Passwords should be different");

    }

    @Test
    public void testHashPasswordWithEmptyPassword() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.passwordManager.hashPassword(
                        ""));
    }

    @Test
    public void testHashPasswordWithNullPassword() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                this.passwordManager.hashPassword(
                        null));
    }

    @Test
    public void testHashPassword() {
        final String password = "PASSWORD";
        Assertions.assertDoesNotThrow(() -> this.passwordManager.hashPassword(
                password));

        String pwd1 = "password";
        String hash1 = this.passwordManager.hashPassword(pwd1);
        assertTrue(this.pbkdf2PasswordHashImpl.verify(pwd1.toCharArray(), hash1));

    }
}
