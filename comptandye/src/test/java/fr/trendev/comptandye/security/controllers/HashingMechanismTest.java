/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fr.trendev.comptandye.security.controllers.HashingMechanism;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class HashingMechanismTest {

    private final HashingMechanism hashingMechanism = new HashingMechanism();

    @Test
    public void testEncrypt_SHA256_base64() throws Exception {
        String word = "Qb7EohqbI2";
        String encrypted = "P7vAmts0c+sxQprYoF5S1Uicvx4RY2J9A+A2FVlThQg=";

        Assertions.assertEquals(hashingMechanism.hash_SHA256_base64(word),
                encrypted);

    }

    @Test
    public void testEncrypt_SHA512_base64() throws Exception {
        String word = "Qb7EohqbI2";
        String encrypted = "AEpNSeN1XHHS5Zah2OTf/zpmlNNtuJRMSCuxgMOCqfBfW6a/c7AsvJZJZGyz0qfxSS2WCOwqq0oZh34tUiU+pQ==";

        Assertions.assertEquals(hashingMechanism.hash_SHA512_base64(word),
                encrypted);
    }

}
