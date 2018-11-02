/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.utils.security.EncryptionUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class EncryptionUtilsTest {

    private EncryptionUtils encryptionUtils = new EncryptionUtils();

    @Test
    public void testEncrypt_SHA256_base64() throws Exception {
        String word = "Qb7EohqbI2";
        String encrypted = "P7vAmts0c+sxQprYoF5S1Uicvx4RY2J9A+A2FVlThQg=";

        Assert.assertEquals(encryptionUtils.encrypt_SHA256_base64(word),
                encrypted);

    }

    @Test
    public void testEncrypt_SHA512_base64() throws Exception {
        String word = "Qb7EohqbI2";
        String encrypted = "AEpNSeN1XHHS5Zah2OTf/zpmlNNtuJRMSCuxgMOCqfBfW6a/c7AsvJZJZGyz0qfxSS2WCOwqq0oZh34tUiU+pQ==";

        Assert.assertEquals(encryptionUtils.encrypt_SHA512_base64(word),
                encrypted);
    }

}
