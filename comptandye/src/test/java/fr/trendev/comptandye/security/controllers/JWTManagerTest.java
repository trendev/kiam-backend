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
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import static fr.trendev.comptandye.security.controllers.JWTManager.ISS;
import static fr.trendev.comptandye.security.controllers.JWTManager.SHORT_VALID_PERIOD;
import static fr.trendev.comptandye.security.controllers.JWTManager.SHORT_VALID_PERIOD_UNIT;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.text.ParseException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
//@RunWith(Parameterized.class)
public class JWTManagerTest {

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(RSAKeyProvider.class,
                    JWTManager.class,
                    JWTWhiteMap.class,
                    JWTRevokedSet.class)
            .inject(this).build();

    @Inject
    private RSAKeyProvider keyProvider;

    @Inject
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
    @Test
    public void testGenerateToken() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "1234567890", false);
            Assertions.assertNotEquals(token.length(), 0);
            Assertions.assertNotNull(token);

            Assertions.assertTrue(this.jwtManager.getWhiteMap().getMap().
                    containsKey(
                            this.caller));

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
            Instant iat = parsedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            Instant exp = parsedJWT.getJWTClaimsSet().getExpirationTime().
                    toInstant();
            Assertions.assertTrue(iat.isBefore(now));
            Assertions.assertTrue(iat.isAfter(now.minus(SHORT_VALID_PERIOD,
                    SHORT_VALID_PERIOD_UNIT)));
            Assertions.assertTrue(exp.equals(iat.plus(SHORT_VALID_PERIOD,
                    SHORT_VALID_PERIOD_UNIT)));

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
    public void testExtractClaimsSet() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token", false);

            Assertions.assertTrue(
                    this.jwtManager.extractClaimsSet(token).isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractClaimsSetWithNullToken() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token", false);

            Assertions.assertFalse(
                    this.jwtManager.extractClaimsSet(null).isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractClaimsSetWithJWTCorruption() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token", false);

            Assertions.assertFalse(
                    this.jwtManager.extractClaimsSet(token.
                            replaceFirst("e", "f"))
                            .isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExtractClaimsSetWithUnverifiedSignature() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups,
                    "x-xsrf-token", false);

            byte[] bytes = token.getBytes();
            bytes[token.length() - 10]++;

            Assertions.assertFalse(
                    this.jwtManager.extractClaimsSet(new String(bytes))
                            .isPresent());

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testIsExpired() throws JOSEException {

        Instant now = Instant.now();
        Instant futur = Instant.now().plus(SHORT_VALID_PERIOD * 2,
                SHORT_VALID_PERIOD_UNIT);

        String token = this.jwtManager.generateToken(this.caller, groups,
                "x-xsrf-token", false);
        Instant exp = this.jwtManager.extractClaimsSet(token)
                .map(JWTClaimsSet::getExpirationTime)
                .map(Date::toInstant)
                .orElseThrow(AssertionError::new);

        Assertions.assertTrue(now.isBefore(exp));
        Assertions.assertFalse(exp.isAfter(futur));

    }

    @Test
    public void testIsRevoked() {
        final String token1 = "token1";
        final Instant now = Instant.now();

        final Date creationDate1 = Date.from(now);
        final Date expirationDate1 = Date.from(now.plus(SHORT_VALID_PERIOD,
                SHORT_VALID_PERIOD_UNIT));

        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);

        Assertions.assertFalse(jwtManager.isRevoked(token1));
        Assertions.assertTrue(jwtManager.getRevokedSet().add(record1));
        Assertions.assertTrue(jwtManager.getRevokedSet().contains(token1));
        Assertions.assertTrue(jwtManager.isRevoked(token1));

    }

    @Test
    public void testCanBeRefreshed() {
        JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();
        Instant now = Instant.now();

        Instant issueTime = now;
        Instant expirationTime = now.plus(5,
                SHORT_VALID_PERIOD_UNIT);

        claimSetBuilder.issueTime(Date.from(issueTime));
        claimSetBuilder.expirationTime(Date.from(expirationTime));
        Assertions.assertFalse(jwtManager.
                canBeRefreshed(claimSetBuilder.build()));

        issueTime = now.minus(2, SHORT_VALID_PERIOD_UNIT);
        expirationTime = issueTime.plus(6, SHORT_VALID_PERIOD_UNIT);

        claimSetBuilder.issueTime(Date.from(issueTime));
        claimSetBuilder.expirationTime(Date.from(expirationTime));
        Assertions.assertFalse(jwtManager.
                canBeRefreshed(claimSetBuilder.build()));

        issueTime = now.minus(4, SHORT_VALID_PERIOD_UNIT);
        expirationTime = issueTime.plus(6, SHORT_VALID_PERIOD_UNIT);

        claimSetBuilder.issueTime(Date.from(issueTime));
        claimSetBuilder.expirationTime(Date.from(expirationTime));
        Assertions.assertTrue(jwtManager.
                canBeRefreshed(claimSetBuilder.build()));

    }

}
