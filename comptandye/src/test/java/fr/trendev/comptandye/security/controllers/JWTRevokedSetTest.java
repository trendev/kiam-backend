/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import javax.inject.Inject;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class JWTRevokedSetTest {

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(JWTRevokedSet.class)
            .inject(this).build();

    @Inject
    JWTRevokedSet jwtRevokedSet;

    public JWTRevokedSetTest() {
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(jwtRevokedSet);
        Assertions.assertDoesNotThrow(() -> jwtRevokedSet.init());
    }

    @Test
    public void testInit() {
    }

    @Test
    public void testClose() {
    }

}
