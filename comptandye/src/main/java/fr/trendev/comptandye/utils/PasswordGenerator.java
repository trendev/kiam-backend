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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class PasswordGenerator {

    private final static String sequence = "azertyuiopmlkjhgfdsqwxcvbn0123456789._-!?@AZERTYUIOPMLKJHGFDSQWXCVBN";
    private final static Random rand = new Random();
    private final static int default_size = 10;

    public static String autoGenerate(int s) {
        String pwd = "";
        int size = (s < default_size) ? default_size : s;

        for (int i = 0; i < size; i++) {
            pwd += sequence.charAt(rand.nextInt(sequence.length()));
        }
        System.out.println("pwd =" + pwd);
        return pwd;
    }

    public static String autoGenerate() {
        return autoGenerate(default_size);
    }

    public static String encrypt_SHA256(String pwd) {
        if (pwd == null || pwd.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password to encrypt cannot be null or empty");
        }
        return encrypt_SHA256_base64(pwd);
    }

    public static String encrypt_SHA256(String pwd, String base) {
        if (pwd == null || pwd.isEmpty()) {
            throw new IllegalArgumentException(
                    "Password to encrypt cannot be null or empty");
        }
        switch (base) {
            case "base64":
                return encrypt_SHA256_base64(pwd);
            case "base16":
                return encrypt_SHA256_base16(pwd);
            default:
                return encrypt_SHA256_base64(pwd);
        }

    }

    private static String encrypt_SHA256_base16(String pwd) {
        String encoded = "NO_ACCESS";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(pwd.getBytes());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).
                        substring(1));
            }
            encoded = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordGenerator.class.getName()).
                    log(Level.SEVERE, "SHA-256 is not a supported algorithm");
        }
        return encoded;

    }

    private static String encrypt_SHA256_base64(String pwd) {
        String encoded = "NO_ACCESS";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pwd.getBytes(StandardCharsets.UTF_8));
            encoded = Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(PasswordGenerator.class.getName()).
                    log(Level.SEVERE, "SHA-256 is not a supported algorithm");
        }
        return encoded;
    }

}
