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

        String email1 = "email1";
        String email2 = "email2";

        String token1 = "token1";
        String token2 = "token2";
        String token3 = "token3";
        Instant now = Instant.now();

        Date creationDate1 = Date.from(now);
        Date creationDate2 = Date.from(now.plus(5, MINUTES));
        Date creationDate3 = Date.from(now.plus(10, MINUTES));
        Date expirationDate1 = Date.from(now.plus(JWTManager.VALID_PERIOD,
                MINUTES));
        Date expirationDate2 = Date.from(now.plus(JWTManager.VALID_PERIOD + 5,
                MINUTES));
        Date expirationDate3 = Date.from(now.plus(JWTManager.VALID_PERIOD + 10,
                MINUTES));

        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Map<String, Set<JWTRecord>> map = jwtwm.getMap();
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        Assertions.assertNull(jwtwm.add(email1, record1));
        Assertions.assertFalse(jwtwm.getMap().isEmpty());
        Assertions.assertTrue(jwtwm.getMap().size() == 1);
        Assertions.assertNull(jwtwm.add(email2, record2));
        Assertions.assertFalse(jwtwm.getMap().isEmpty());
        Assertions.assertTrue(jwtwm.getMap().size() == 2);

        Set<JWTRecord> email1Records = jwtwm.add(email1, record3);
        Assertions.assertNotNull(email1Records);
        Assertions.assertTrue(email1Records.size() == 2);
        Assertions.assertTrue(email1Records.contains(record1));
        Assertions.assertTrue(email1Records.contains(record3));

        Assertions.assertFalse(jwtwm.getMap().isEmpty());
        Assertions.assertTrue(jwtwm.getMap().size() == 2);

        // test unique element
        email1Records = jwtwm.add(email1, record3);
        Assertions.assertNotNull(email1Records);
        Assertions.assertTrue(email1Records.size() == 2);
        Assertions.assertTrue(email1Records.contains(record1));
        Assertions.assertTrue(email1Records.contains(record3));

    }

}
