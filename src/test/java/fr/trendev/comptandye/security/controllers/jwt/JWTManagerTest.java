/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt;

import com.nimbusds.jose.JOSEException;
import static com.nimbusds.jose.JOSEObjectType.JWT;
import static com.nimbusds.jose.JWSAlgorithm.RS256;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fr.trendev.comptandye.security.controllers.MockAuthenticationEventController;
import fr.trendev.comptandye.security.controllers.RSAKeyProvider;
import static fr.trendev.comptandye.security.controllers.jwt.JWTManager.ISS;
import static fr.trendev.comptandye.security.controllers.jwt.JWTManager.LONG_TERM_VALIDITY;
import static fr.trendev.comptandye.security.controllers.jwt.JWTManager.LONG_TERM_VALIDITY_UNIT;
import static fr.trendev.comptandye.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY;
import static fr.trendev.comptandye.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY_UNIT;
import fr.trendev.comptandye.security.controllers.jwt.dto.mock.MockJWTRevokedSetDTO;
import fr.trendev.comptandye.security.controllers.jwt.dto.mock.MockJWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
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
                    MockAuthenticationEventController.class,
                    MockJWTWhiteMapDTO.class,
                    JWTRevokedSet.class,
                    MockJWTRevokedSetDTO.class)
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
    public void testCreateShortTermToken() {
        this.testCreateToken(false);
    }

    @Test
    public void testCreateLongTermToken() {
        this.testCreateToken(true);
    }

    private void testCreateToken(boolean rmbme) {
        try {
            String token = this.jwtManager.createToken(this.caller, groups,
                    "1234567890", rmbme);
            Assertions.assertNotEquals(token.length(), 0);
            Assertions.assertNotNull(token);

            Assertions.assertTrue(this.jwtManager.getJWTWhiteMap().getMap().
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
            Assertions.assertTrue(iat.isAfter(now.minus(
                    rmbme ? LONG_TERM_VALIDITY : SHORT_TERM_VALIDITY,
                    rmbme ? LONG_TERM_VALIDITY_UNIT : SHORT_TERM_VALIDITY_UNIT)));
            Assertions.assertTrue(exp.equals(iat.plus(
                    rmbme ? LONG_TERM_VALIDITY : SHORT_TERM_VALIDITY,
                    rmbme ? LONG_TERM_VALIDITY_UNIT : SHORT_TERM_VALIDITY_UNIT)));

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
            String token = this.jwtManager.createToken(this.caller, groups,
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
            String token = this.jwtManager.createToken(this.caller, groups,
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
            String token = this.jwtManager.createToken(this.caller, groups,
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
            String token = this.jwtManager.createToken(this.caller, groups,
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
    public void testHasExpired() throws JOSEException {

        String token = this.jwtManager.createToken(this.caller, groups,
                "x-xsrf-token", false);
        JWTClaimsSet claimsSet = this.jwtManager.extractClaimsSet(token).get();

        Assertions.assertFalse(this.jwtManager.hasExpired(claimsSet));

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
        Instant now = Instant.now();
        Instant exp = now.minus(SHORT_TERM_VALIDITY, SHORT_TERM_VALIDITY_UNIT);
        builder.expirationTime(Date.from(exp));

        Assertions.assertTrue(this.jwtManager.hasExpired(builder.build()));
    }

    @Test
    public void testIsRevoked() {
        final String token1 = "token1";
        final Instant now = Instant.now();

        final Date creationDate1 = Date.from(now);
        final Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));

        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);

        Assertions.assertFalse(jwtManager.isRevoked(token1));
        Assertions.assertTrue(jwtManager.getJWTRevokedSet().add(record1));
        Assertions.assertTrue(jwtManager.getJWTRevokedSet().contains(token1));
        Assertions.assertTrue(jwtManager.isRevoked(token1));

    }

    @Test
    public void testCanBeRefreshed() {
        JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();
        Instant now = Instant.now();

        // controls a fresh new token cannot be refreshed
        Instant iat = now;
        Instant exp = now.plus(SHORT_TERM_VALIDITY, SHORT_TERM_VALIDITY_UNIT);

        claimSetBuilder.issueTime(Date.from(iat));
        claimSetBuilder.expirationTime(Date.from(exp));
        Assertions.assertFalse(jwtManager.
                canBeRefreshed(claimSetBuilder.build()));

        // controls a token in the refresh zone can be refreshed
        Instant refreshzone = this.jwtManager.refreshZone(iat, exp);
        exp = now.plusMillis(exp.toEpochMilli() - refreshzone.toEpochMilli());
        iat = exp.minus(SHORT_TERM_VALIDITY, SHORT_TERM_VALIDITY_UNIT);

        claimSetBuilder.issueTime(Date.from(iat));
        claimSetBuilder.expirationTime(Date.from(exp));
        Assertions.assertTrue(jwtManager.
                canBeRefreshed(claimSetBuilder.build()));

    }

    @Test
    public void testGetJWTWhiteMap() {
        Assertions.assertNotNull(this.jwtManager.getJWTWhiteMap());
    }

    @Test
    public void testGetJWTRevokedSet() {
        Assertions.assertNotNull(this.jwtManager.getJWTRevokedSet());
    }

    @Test
    public void testRevokeToken() {

        final String email1 = "skonx2006@hotmail.com";
        final String token1 = "token1RevokeToken";
        final Instant now = Instant.now();

        final Date creationDate1 = Date.from(now);
        final Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));

        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);

        Assertions.assertFalse(jwtManager.isRevoked(token1));
        Assertions.assertFalse(jwtManager.getJWTWhiteMap().add(email1, record1)
                .isPresent());// empty set at first
        Assertions.assertTrue(jwtManager.getJWTWhiteMap().getMap().containsKey(
                email1));
        Assertions.assertTrue(jwtManager.getJWTWhiteMap().getRecords(email1).
                isPresent());

        Optional<JWTRecord> opt = jwtManager.revokeToken(email1, token1);

        Assertions.assertTrue(opt.isPresent());
        Assertions.assertFalse(jwtManager.getJWTWhiteMap().getMap().containsKey(
                email1));
        Assertions.assertFalse(jwtManager.getJWTWhiteMap().getRecords(email1).
                isPresent());

        Assertions.assertTrue(jwtManager.getJWTRevokedSet().contains(token1));
        Assertions.assertTrue(jwtManager.isRevoked(token1));
    }

    @Test
    public void testRevokeAllTokens() throws InterruptedException {
        String email1 = "skonx2006@hotmail.com";
        String token1 = "t1RvkAll";
        String token2 = "t2RvkAll";
        String token3 = "t3RvkAll";
        Instant now = Instant.now();

        Date creationDate1 = Date.from(now);
        Date creationDate2 = Date.from(now.plus(5, ChronoUnit.SECONDS));
        Date creationDate3 = Date.from(now.plus(10, ChronoUnit.SECONDS));
        Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        Date expirationDate2 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT).plus(5, ChronoUnit.SECONDS));
        Date expirationDate3 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT).plus(10, ChronoUnit.SECONDS));

        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Assertions.assertFalse(jwtManager.getJWTWhiteMap().add(email1, record1)
                .isPresent());// empty set at first
        Assertions.assertTrue(jwtManager.getJWTWhiteMap().add(email1, record2)
                .isPresent());// empty set at first
        Assertions.assertTrue(jwtManager.getJWTWhiteMap().add(email1, record3)
                .isPresent());// empty set at first

        Optional<Set<JWTRecord>> records =
                this.jwtManager.getJWTWhiteMap().getRecords(email1);

        Assertions.assertTrue(records.isPresent());
        Assertions.assertEquals(records.get().size(), 3);

        Assertions.assertFalse(this.jwtManager.isRevoked(token1));
        Assertions.assertFalse(this.jwtManager.isRevoked(token2));
        Assertions.assertFalse(this.jwtManager.isRevoked(token3));

        Optional<Set<JWTRecord>> opt = this.jwtManager.revokeAllTokens(email1);
        Assertions.assertTrue(opt.isPresent());
        Assertions.assertEquals(records.get(), opt.get());
    }

    @Test
    public void testRefreshToken() {
        try {
            String xsrf = UUID.randomUUID().toString();
            String token = this.jwtManager.createToken(this.caller, groups,
                    xsrf, false);

            this.jwtManager.extractClaimsSet(token).ifPresent(cs_ -> {

                JWTClaimsSet cs = cs_;

                try {
                    for (int i = 0; i < 10; i++) {
                        String newToken = this.jwtManager.refreshToken(cs);
                        Assertions.assertNotNull(newToken);
                        Assertions.assertFalse(newToken.isEmpty());

                        Assertions.assertTrue(this.jwtManager.extractClaimsSet(
                                newToken).isPresent());

                        JWTClaimsSet ncs = this.jwtManager.extractClaimsSet(
                                newToken).get();

                        Assertions.assertEquals(ncs.getStringClaim("xsrf"),
                                xsrf);
                        Assertions.assertEquals(ncs.getIntegerClaim("refresh").
                                doubleValue(),
                                i + 1, "refresh = " + ncs.getIntegerClaim(
                                        "refresh").
                                        doubleValue() + " / i+1 =" + (i + 1)
                                + " / i = " + i);

                        Assertions.assertNotEquals(cs.getJWTID(),
                                ncs.getJWTID());

                        Assertions.assertEquals(cs.getSubject(), ncs.
                                getSubject());
                        Assertions.assertEquals(cs.getStringClaim("upn"), ncs.
                                getStringClaim("upn"));
                        Assertions.assertFalse(Collections
                                .disjoint(
                                        cs.getStringListClaim("groups"),
                                        ncs.getStringListClaim("groups")));
                        Assertions.assertEquals(
                                cs.getExpirationTime().getTime()
                                - cs.getIssueTime().getTime(),
                                ncs.getExpirationTime().getTime()
                                - ncs.getIssueTime().getTime());

                        cs = ncs;
                    }

                } catch (ParseException ex) {
                    Logger.getLogger(JWTManagerTest.class.getName()).
                            log(Level.SEVERE, null, ex);
                } catch (JOSEException ex) {
                    Logger.getLogger(JWTManagerTest.class.getName()).
                            log(Level.SEVERE, null, ex);
                }

            });

        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
