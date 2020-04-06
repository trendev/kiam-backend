/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class DefaultHttpAuthenticationMechanismTest {

    public DefaultHttpAuthenticationMechanismTest() {
    }

    @Test
    public void testRememberMe() throws Exception {
        Assertions.assertTrue(Boolean.valueOf("true"));
        Assertions.assertFalse(Boolean.valueOf("false"));
        Assertions.assertFalse(Boolean.valueOf("not_a_boolean"));
        Assertions.assertFalse(Boolean.valueOf(null));
    }

}
