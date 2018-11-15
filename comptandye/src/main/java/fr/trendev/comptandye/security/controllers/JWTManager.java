/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.io.IOException;
import static java.lang.Thread.currentThread;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class JWTManager {

    private static final Logger LOG = Logger.getLogger(JWTManager.class.
            getName());

    public PrivateKey readPrivateKey(String resourceName) {
        try {
            String key = this.readKey(resourceName);
            return KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(
                            Base64.getDecoder().
                                    decode(key)));
        } catch (IOException | NoSuchAlgorithmException |
                InvalidKeySpecException ex) {
            LOG.log(Level.SEVERE,
                    "Exception reading the private key in JWTManager", ex);
            throw new WebApplicationException(
                    "Exception reading the private key in JWTManager", ex);
        }
    }

    public RSAPublicKey readPublicKey(String resourceName) {
        try {
            String key = this.readKey(resourceName);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(
                            Base64.getDecoder().
                                    decode(key)));
            return pubKey;
        } catch (IOException | NoSuchAlgorithmException |
                InvalidKeySpecException ex) {
            LOG.log(Level.SEVERE,
                    "Exception reading the public key in JWTManager", ex);
            throw new WebApplicationException(
                    "Exception reading the public key in JWTManager", ex);
        }
    }

    private final String readKey(String resourceName) throws IOException {
        byte[] byteBuffer = new byte[16384];
        int length = currentThread().getContextClassLoader()
                .getResource(resourceName)
                .openStream()
                .read(byteBuffer);

        return new String(byteBuffer, 0, length).replaceAll(
                "-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "")
                .trim();
    }
}
