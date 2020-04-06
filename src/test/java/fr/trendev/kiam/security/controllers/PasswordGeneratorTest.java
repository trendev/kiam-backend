/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import fr.trendev.kiam.security.controllers.PasswordManager;
import java.util.stream.IntStream;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.glassfish.soteria.identitystores.hash.Pbkdf2PasswordHashImpl;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
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

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(PasswordManager.class, Pbkdf2PasswordHashImpl.class)
            .inject(this).build();

    @Inject
    private PasswordManager passwordManager;

    @Inject
    private Pbkdf2PasswordHash pbkdf2PasswordHash;

    public PasswordGeneratorTest() {
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
        assertTrue(this.pbkdf2PasswordHash.verify(pwd1.toCharArray(), hash1));

    }
}
