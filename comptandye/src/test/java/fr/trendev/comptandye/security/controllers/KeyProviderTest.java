/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class KeyProviderTest {

    private KeyProvider keyProvider;

    public KeyProviderTest() {
    }

    @Before
    public void init() {
        this.keyProvider = new KeyProvider();
    }

    @Test
    public void testGetPrivateKey() {
        Assertions.assertDoesNotThrow(this.keyProvider::init);
        Assertions.assertNotNull(this.keyProvider.getPrivateKey());
    }

    @Test
    public void testGetPublicKey() {
        Assertions.assertDoesNotThrow(this.keyProvider::init);
        Assertions.assertNotNull(this.keyProvider.getPublicKey());
    }

    @Test
    public void testInit() {
        Assertions.assertNotNull(this.keyProvider);
        Assertions.assertDoesNotThrow(this.keyProvider::init);
    }

}
