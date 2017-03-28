/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class PasswordGenerator {

    public static String autoGenerate() {
        return encrypt_SHA256(UUIDGenerator.generate(true));
    }

    public static String encrypt_SHA256(String pwd) {
        String spwd = "NO_ACCESS";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pwd.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).
                        substring(1));
            }
            spwd = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordGenerator.class.getName()).
                    log(Level.SEVERE, "SHA-256 is not a supported algorithm");
        }

        return spwd;

    }

}
