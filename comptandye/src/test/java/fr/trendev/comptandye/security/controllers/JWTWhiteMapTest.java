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
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
public class JWTWhiteMapTest {

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
            .from(JWTWhiteMap.class)
            .inject(this).build();

    @Inject
    JWTWhiteMap jwtwm;

    public JWTWhiteMapTest() {
    }

    @Before
    public void init() {
        if (jwtwm != null) {
            jwtwm.clear();
        }
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(jwtwm);
        Assertions.assertDoesNotThrow(() -> jwtwm.getMap());
    }

    @Test
    public void testInit() {
    }

    @Test
    public void testClose() {
    }

    @Test
    public void testGetMap() {
        Map<String, Set<JWTRecord>> map = jwtwm.getMap();
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());
    }

    @Test
    public void testClear() {
        jwtwm.clear();
        Assertions.assertTrue(jwtwm.getMap().isEmpty());
    }

    @Test
    public void testAdd() {

        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Map<String, Set<JWTRecord>> map = jwtwm.getMap();
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        Assertions.assertFalse(jwtwm.add(email1, record1).isPresent());
        Assertions.assertFalse(jwtwm.getMap().isEmpty());
        Assertions.assertTrue(jwtwm.getMap().size() == 1);
        Assertions.assertFalse(jwtwm.add(email2, record2).isPresent());
        Assertions.assertFalse(jwtwm.getMap().isEmpty());
        Assertions.assertTrue(jwtwm.getMap().size() == 2);

        Optional<Set<JWTRecord>> opt = jwtwm.add(email1, record3);
        Assertions.assertTrue(opt.isPresent());
        Set<JWTRecord> email1Records = opt.get();
        Assertions.assertNotNull(email1Records);
        Assertions.assertTrue(email1Records.size() == 2);
        Assertions.assertTrue(email1Records.contains(record1));
        Assertions.assertTrue(email1Records.contains(record3));

        Assertions.assertFalse(jwtwm.getMap().isEmpty());
        Assertions.assertTrue(jwtwm.getMap().size() == 2);

        // test unique element
        opt = jwtwm.add(email1, record3);
        Assertions.assertTrue(opt.isPresent());
        email1Records = opt.get();
        Assertions.assertNotNull(email1Records);
        Assertions.assertTrue(email1Records.size() == 2);
        Assertions.assertTrue(email1Records.contains(record1));
        Assertions.assertTrue(email1Records.contains(record3));

    }

    @Test
    public void testGetTokens() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Map<String, Set<JWTRecord>> map = jwtwm.getMap();
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        Assertions.assertFalse(jwtwm.getTokens("fake-email").isPresent());

        jwtwm.add(email1, record1);
        jwtwm.add(email2, record2);
        jwtwm.add(email1, record3);

        Assertions.assertTrue(jwtwm.getTokens(email1).isPresent());
        Assertions.assertTrue(jwtwm.getTokens(email2).isPresent());

        Optional<Set<JWTRecord>> opt = jwtwm.getTokens(email1);
        Set<JWTRecord> email1Records = opt.get();
        Assertions.assertNotNull(email1Records);
        Assertions.assertTrue(email1Records.size() == 2);
        Assertions.assertTrue(email1Records.contains(record1));
        Assertions.assertTrue(email1Records.contains(record3));

    }

    @Test
    public void testRemove_String_String() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        jwtwm.add(email1, record1);
        jwtwm.add(email2, record2);
        jwtwm.add(email1, record3);

        Set<JWTRecord> records = jwtwm.getTokens(email1).get();

        Assertions.assertFalse(jwtwm.remove("fake-email", token1).isPresent());
        Assertions.assertTrue(jwtwm.remove(email1, token1).isPresent());
        Assertions.assertTrue(records.size() == 1);
        Assertions.assertTrue(jwtwm.remove(email1, token3).isPresent());
        Assertions.assertTrue(records.isEmpty());
        Assertions.assertFalse(jwtwm.getMap().containsKey(email1));
        Assertions.assertFalse(jwtwm.remove(email1, token1).isPresent());

        Assertions.assertTrue(jwtwm.remove(email2, token2).isPresent());
        Assertions.assertFalse(jwtwm.getMap().containsKey(email2));
        Assertions.assertTrue(jwtwm.getMap().isEmpty());

    }

    @Test
    public void testRemove_String() {
    }

}
