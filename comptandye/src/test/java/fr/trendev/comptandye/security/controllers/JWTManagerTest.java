/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class JWTManagerTest {

    private final String secret;
    private final String iss;
    private final String sub;
    private final String aud;
    private final Instant current_time;
    private final Instant expiration_time;
    private final Date iat;
    private final Date nbf;
    private final Date exp;
    private final String jti;

    public JWTManagerTest() {
        this.secret = "Iyi0KWCnZpwkr-HIVH1spXfSB2IiaTAk";
        this.iss = "www.comptandye.fr";
        this.sub = "julien.sie@gmail.com";
        this.aud = "comptandye";
        this.current_time = Instant.now();
        this.expiration_time = this.current_time.plus(3,
                ChronoUnit.MINUTES);
        this.iat = Date.from(current_time);
        this.nbf = Date.from(current_time);
        this.exp = Date.from(expiration_time);
        this.jti = UUID.randomUUID().toString();
    }

    @Test
    public void testCreateJWT() {
        try {
            JWSSigner signer = new MACSigner(this.secret);

            // Prepare JWT with claims set
            JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();

            claimSetBuilder.issuer(this.iss);
            claimSetBuilder.subject(this.sub);
            claimSetBuilder.audience(this.aud);
            claimSetBuilder.issueTime(this.iat);
            claimSetBuilder.notBeforeTime(this.nbf);
            claimSetBuilder.expirationTime(this.exp);
            claimSetBuilder.jwtID(this.jti);

            JWTClaimsSet claimsSet = claimSetBuilder.build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            // apply the HMAC protection
            signedJWT.sign(signer);

            // serialize the compact form
            String jwt = signedJWT.serialize();
            System.out.println(jwt);
            Assertions.assertNotNull(jwt);

        } catch (KeyLengthException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
