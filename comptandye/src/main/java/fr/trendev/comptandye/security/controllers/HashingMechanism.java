/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.ejb.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class HashingMechanism {

    public String hash_SHA256_base16(String word) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(word.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).
                        substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(
                    "SHA-256 is not a supported algorithm", ex);
        }

    }

    public String hash_SHA256_base64(String word) {
        return this.hash_SHA_base64(word, "SHA-256");
    }

    public String hash_SHA512_base64(String word) {
        return this.hash_SHA_base64(word, "SHA-512");
    }

    private String hash_SHA_base64(String word, String algo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            byte[] hash = md.digest(word.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(
                    algo + " is not a supported algorithm", ex);
        }
    }
}
