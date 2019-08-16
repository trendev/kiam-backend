/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.useraccount.controllers;

import java.util.Arrays;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class EmailValidatorTest {

    private final EmailValidator validator;

    public EmailValidatorTest() {
        this.validator = new EmailValidator();
    }

    @Test
    public void testValid() {

        Arrays.asList(
                "user@domain.com",
                "user@domain.co.in",
                "user.name@domain.com",
                "user_name@domain.com",
                "username@yahoo.corporate.in",
                "user?name@domain.co.in",
                "name.name@name-names.domain.no",
                "firstname-lastname@name2.names.domain.no",
                "demo@comptandye.fr",
                "skonx2006@hotmail.com"
        ).forEach(e
                -> Assertions.assertTrue(validator.valid(e),
                        e + " should be valid"));

        Arrays.asList(
                ".username@yahoo.com",
                "username@yahoo.com.",
                "username@yahoo..com",
                "username@yahoo.c",
                "username@yahoo.corporate"
        ).forEach(e
                -> Assertions.assertFalse(validator.valid(e),
                        e + " should not be valid"));
    }

}
