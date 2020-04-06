/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import fr.trendev.kiam.security.controllers.RSAKeyProvider;
import javax.inject.Inject;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class RSAKeyProviderTest {

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(RSAKeyProvider.class)
            .inject(this).build();

    @Inject
    private RSAKeyProvider keyProvider;

    public RSAKeyProviderTest() {
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
