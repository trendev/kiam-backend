/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import com.nimbusds.jose.JOSEException;
import static com.nimbusds.jose.JOSEObjectType.JWT;
import static com.nimbusds.jose.JWSAlgorithm.RS256;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import static fr.trendev.comptandye.security.controllers.JWTManager.ISS;
import static fr.trendev.comptandye.security.controllers.JWTManager.VALID_PERIOD;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
//@RunWith(Parameterized.class)
public class JWTManagerTest {

    private RSAKeyProvider keyProvider;
    private JWTManager jwtManager;

    private final String caller = "julien.sie@gmail.com";
    private final List<String> groups;

    public JWTManagerTest() {
        this.groups = Arrays.asList(new String[]{"Group2", "Group1",});
    }

//    @Parameterized.Parameters
//    public static Object[][] data() {
//        return new Object[3][0];
//    }
    @Before
    public void init() {
        this.keyProvider = new RSAKeyProvider();
        this.keyProvider.init();
        this.jwtManager = new JWTManager(this.keyProvider.getPrivateKey(),
                this.keyProvider.getPublicKey());
        this.jwtManager.init();
    }

    @Test
    public void testGenerateToken() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "1234567890");
            Assertions.assertNotEquals(token.length(), 0);
            Assertions.assertNotNull(token);

            SignedJWT parsedJWT = SignedJWT.parse(token);
            Assertions.assertNotNull(parsedJWT);

            JWSVerifier verifier = new RSASSAVerifier(this.keyProvider.
                    getPublicKey());

            Assertions.assertTrue(parsedJWT.verify(verifier));
            Assertions.
                    assertEquals(parsedJWT.getJWTClaimsSet().getIssuer(), ISS);
            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getSubject(),
                    this.caller);
            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getStringClaim(
                    "upn"),
                    this.caller);

            Instant now = Instant.now();
            Instant iat = Instant.ofEpochMilli(parsedJWT.getJWTClaimsSet().
                    getIssueTime().getTime());
            Instant exp = Instant.ofEpochMilli(parsedJWT.getJWTClaimsSet().
                    getExpirationTime().getTime());
            Assertions.assertTrue(iat.isBefore(now));
            Assertions.assertTrue(iat.isAfter(now.minus(VALID_PERIOD,
                    ChronoUnit.MINUTES)));
            Assertions.assertTrue(exp.equals(iat.plus(VALID_PERIOD,
                    ChronoUnit.MINUTES)));

            Assertions.assertIterableEquals(this.groups, parsedJWT.
                    getJWTClaimsSet().
                    getStringListClaim("groups"));

            Assertions.assertEquals(parsedJWT.getHeader().getAlgorithm(), RS256);
            Assertions.assertEquals(parsedJWT.getHeader().getType(), JWT);
            Assertions.assertEquals(parsedJWT.getHeader().getKeyID(),
                    "privateKey.pem");

        } catch (KeyLengthException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (JOSEException | ParseException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testGetClaimsSet() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token");

            Assertions.assertTrue(
                    this.jwtManager.getClaimsSet(token).isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testGetClaimsSetWithNullToken() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token");

            Assertions.assertFalse(
                    this.jwtManager.getClaimsSet(null).isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testGetClaimsSetWithJWTCorruption() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token");

            Assertions.assertFalse(
                    this.jwtManager.getClaimsSet(token.replaceFirst("e", "f"))
                            .isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testGetClaimsSetWithUnverifiedSignature() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token");

            byte[] bytes = token.getBytes();
            bytes[token.length() - 10]++;

            Assertions.assertFalse(
                    this.jwtManager.getClaimsSet(new String(bytes))
                            .isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
