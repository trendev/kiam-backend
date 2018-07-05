/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

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
public class EncryptionUtils {

    public String encrypt_SHA256_base16(String pwd) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pwd.getBytes());

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

    public String encrypt_SHA256_base64(String pwd) {
        return this.encrypt_SHA_base64(pwd, "SHA-256");
    }

    public String encrypt_SHA512_base64(String pwd) {
        return this.encrypt_SHA_base64(pwd, "SHA-512");
    }

    private String encrypt_SHA_base64(String pwd, String algo) {
        try {
            MessageDigest md = MessageDigest.getInstance(algo);
            byte[] hash = md.digest(pwd.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException(
                    algo + " is not a supported algorithm", ex);
        }
    }
}
