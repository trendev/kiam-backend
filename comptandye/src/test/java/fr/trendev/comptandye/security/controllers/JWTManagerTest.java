/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.PrivateKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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

    private final JWTManager jwtManager;

    private final String iss;
    private final String sub;
    private final String aud;
    private final Instant current_time;
    private final Instant expiration_time;
    private final Date iat;
    private final Date exp;
    private final String jti;
    private final List<String> groups;

    public JWTManagerTest() {
        this.jwtManager = new JWTManager();

        this.iss = "https://www.comptandye.fr";
        this.sub = "julien.sie@gmail.com";
        this.aud = "comptandye";
        this.current_time = Instant.now();
        this.expiration_time = this.current_time.plus(3,
                ChronoUnit.MINUTES);
        this.iat = Date.from(current_time);
        this.exp = Date.from(expiration_time);
        this.jti = UUID.randomUUID().toString();
        this.groups = Arrays.asList(new String[]{"Professional",
            "Administrator"});
    }

    @Test
    public void testSignedJWT() {
        try {
            PrivateKey pkey = this.jwtManager.readPrivateKey(
                    "privateKey.pem");

            // Prepare JWT with claims set
            JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();
            claimSetBuilder.issuer(this.iss);
            claimSetBuilder.subject(this.sub);
            claimSetBuilder.audience(this.aud);
            claimSetBuilder.issueTime(this.iat);
            claimSetBuilder.expirationTime(this.exp);
            claimSetBuilder.jwtID(this.jti);

            //MP-JWT specific
            claimSetBuilder.claim("upn", this.sub);
            claimSetBuilder.claim("groups", this.groups);

            JWTClaimsSet claimsSet = claimSetBuilder.build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader.Builder(JWSAlgorithm.RS256)
                            .keyID("/privateKey.pem")
                            .type(JOSEObjectType.JWT)
                            .build(), claimsSet);

            signedJWT.sign(new RSASSASigner(pkey));

            String token = signedJWT.serialize();
            System.out.println(token);
            Assertions.assertNotEquals(token.length(), 0);
            Assertions.assertNotNull(token);

            SignedJWT parsedJWT = SignedJWT.parse(token);
            Assertions.assertNotNull(parsedJWT);

//            RSAPublicKey publicKey = KeyFactory.getInstance("RSA")
//                    .
//                    
//                    
//            JWSVerifier verifier = new RSASSAVerifier(publicKey);
//
//            Assertions.assertTrue(parsedJWT.verify(verifier));
            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getJWTID(),
                    this.jti);
            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getIssuer(),
                    this.iss);
            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getSubject(),
                    this.sub);
            Assertions.assertTrue(
                    (this.iat.getTime() / 1000)
                    == (parsedJWT.getJWTClaimsSet().getIssueTime()
                            .getTime() / 1000));
            Assertions.assertEquals(this.sub, parsedJWT.getJWTClaimsSet().
                    getStringClaim("upn"));
            Assertions.assertIterableEquals(this.groups, parsedJWT.
                    getJWTClaimsSet().
                    getStringListClaim("groups"));

        } catch (KeyLengthException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testReadPrivateKey() throws Exception {
        Assertions.assertNotNull(this.jwtManager.
                readPrivateKey("privateKey.pem"));
    }

}
