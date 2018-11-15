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
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
            byte[] byteBuffer = new byte[16384];
            int length = currentThread().getContextClassLoader()
                    .getResource(resourceName)
                    .openStream()
                    .read(byteBuffer);

            String key = new String(byteBuffer, 0, length).replaceAll(
                    "-----BEGIN (.*)-----", "")
                    .replaceAll("-----END (.*)----", "")
                    .replaceAll("\r\n", "")
                    .replaceAll("\n", "")
                    .trim();

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
}
