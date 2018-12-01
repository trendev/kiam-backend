/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fr.trendev.comptandye.security.entities.JWTRecord;
import java.time.Instant;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.Date;
import javax.inject.Inject;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class JWTRevokedSetTest {

    private final String email1 = "email1";
    private final String email2 = "email2";

    private final String token1 = "token1";
    private final String token2 = "token2";
    private final String token3 = "token3";
    private final Instant now = Instant.now();

    private final Date creationDate1 = Date.from(now);
    private final Date creationDate2 = Date.from(now.plus(5, MINUTES));
    private final Date creationDate3 = Date.from(now.plus(10, MINUTES));
    private final Date expirationDate1 = Date.from(now.plus(
            JWTManager.VALID_PERIOD,
            MINUTES));
    private final Date expirationDate2 = Date.from(now.plus(
            JWTManager.VALID_PERIOD + 5,
            MINUTES));
    private final Date expirationDate3 = Date.from(now.plus(
            JWTManager.VALID_PERIOD + 10,
            MINUTES));

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(JWTRevokedSet.class)
            .inject(this).build();

    @Inject
    JWTRevokedSet jwtRevokedSet;

    public JWTRevokedSetTest() {
    }

    @Before
    public void init() {
        if (jwtRevokedSet != null) {
            jwtRevokedSet.clear();
        }
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(jwtRevokedSet);
        Assertions.assertDoesNotThrow(() -> jwtRevokedSet.init());
    }

    @Test
    public void testInit() {
    }

    @Test
    public void testClose() {
    }

    @Test
    public void testGetSet() {
        Assertions.assertNotNull(jwtRevokedSet.getSet());
        Assertions.assertTrue(jwtRevokedSet.getSet().isEmpty());
    }

    @Test
    public void testAdd() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Assertions.assertTrue(jwtRevokedSet.add(record1));
        Assertions.assertTrue(jwtRevokedSet.getSet().size() == 1);
        Assertions.assertTrue(jwtRevokedSet.add(record2));
        Assertions.assertTrue(jwtRevokedSet.getSet().size() == 2);
        Assertions.assertTrue(jwtRevokedSet.add(record3));
        Assertions.assertTrue(jwtRevokedSet.getSet().size() == 3);

        //control duplication
        Assertions.assertFalse(jwtRevokedSet.add(record1));
        Assertions.assertTrue(jwtRevokedSet.getSet().size() == 3);
    }

    @Test
    public void testContains() {
    }

    @Test
    public void testRemove_String() {
    }

    @Test
    public void testRemove_JWTRecord() {
    }

    @Test
    public void testClear() {
        jwtRevokedSet.clear();
        Assertions.assertTrue(jwtRevokedSet.getSet().isEmpty());
    }

}
