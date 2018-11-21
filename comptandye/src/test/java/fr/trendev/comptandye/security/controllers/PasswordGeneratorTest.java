/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;
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

    public PasswordGeneratorTest() {
    }

    @Before
    public void init() {
        this.passwordManager = new PasswordManager(new HashingMechanism());
    }

    @Test
    public void testConstructors() {
        PasswordManager passwordManager = new PasswordManager();
        Assertions.assertThrows(NullPointerException.class,
                () -> passwordManager.hashPassword("PASSWORD"));

    }

    /**
     * Test of autoGenerate method, of class PasswordManager.
     */
    @Test
    public void testAutoGenerate() {
        System.out.println("autoGenerate");

        int n = 5;
        int size = 12;
        Set<String> spwd = new HashSet<>(n);

        assertTrue(spwd.isEmpty());

        IntStream.range(0, n).forEach(i -> spwd.add(passwordManager.
                hashPassword(passwordManager.
                        autoGenerate(size))));

        assertFalse(spwd.isEmpty());

        System.out.println(n + " secured passwords generated");
        if (spwd.size() != n) {
            System.out.println(spwd.size() * 100 / n + "% are different");
        } else {
            System.out.println("They are all different");
        }

    }

    /**
     * Test of hashPassword method, of class PasswordManager.
     */
    @Test
    public void testEncrypt_SHA256() {
        System.out.println("** encrypt_SHA256 **");

        System.out.println("encrypt_SHA256 Base64");
        System.out.println("---------------------");

        assertPasswordEncryption("error",
                "ygD8z7QImJ7dxAEGLE0SGaas62ubVUEjV/F5CGLo8Xg=");

        assertPasswordEncryption("password",
                "XohImNooBHFR0OVvjcYpJ3NgPQ1qq73WKhHvch0VQtg=");

        assertPasswordEncryption("comptandye_password",
                "8oOK6BsXsv+s19XMaKMUYh6y0IvVf4Kjb7GmSWS/M0Y=");
    }

    private void assertPasswordEncryption(String pwd, String pwd_sha256) {
        String hpwd = passwordManager.hashPassword(pwd);
        System.out.println("\"" + pwd + "\" ==> " + hpwd);
        assertEquals(pwd_sha256, hpwd);
    }

    @Test
    public void testHashPasswordWithEmptyPassword() {

    }

    @Test
    public void testHashPasswordWithNullPassword() {

    }

    @Test
    public void testHashPassword() {
        final String password = "PASSWORD";
        Assertions.assertDoesNotThrow(() -> this.passwordManager.hashPassword(
                password));
    }
}
