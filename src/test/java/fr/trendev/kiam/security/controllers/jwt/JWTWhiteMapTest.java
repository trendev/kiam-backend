/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt;

import fr.trendev.kiam.security.controllers.jwt.JWTWhiteMap;
import fr.trendev.kiam.security.controllers.MockAuthenticationEventController;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY_UNIT;
import fr.trendev.kiam.security.controllers.jwt.dto.mock.MockJWTWhiteMapDTO;
import fr.trendev.kiam.security.entities.JWTRecord;
import java.time.Instant;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.IntStream;
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
            SHORT_TERM_VALIDITY,
            MINUTES));
    private final Date expirationDate2 = Date.from(now.plus(
            SHORT_TERM_VALIDITY + 5,
            MINUTES));
    private final Date expirationDate3 = Date.from(now.plus(
            SHORT_TERM_VALIDITY + 10,
            MINUTES));
    private static final Logger LOG = Logger.getLogger(JWTWhiteMapTest.class.
            getName());

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(JWTWhiteMap.class,
                    MockAuthenticationEventController.class,
                    MockJWTWhiteMapDTO.class)
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
        LOG.info("### TEST JWTWHITEMAP INIT ###");
        Assertions.assertDoesNotThrow(() -> jwtwm.init());
        Assertions.assertFalse(jwtwm.getMap().isEmpty(),
                "jwtwm.getMap() should not be empty");
        Assertions.assertTrue(jwtwm.getMap().size() == 1,
                "jwtwm.getMap().size() should be 1");
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
    public void testGetRecords() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Map<String, Set<JWTRecord>> map = jwtwm.getMap();
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        Assertions.assertFalse(jwtwm.getRecords("fake-email").isPresent());

        jwtwm.add(email1, record1);
        jwtwm.add(email2, record2);
        jwtwm.add(email1, record3);

        Assertions.assertTrue(jwtwm.getRecords(email1).isPresent());
        Assertions.assertTrue(jwtwm.getRecords(email2).isPresent());

        Optional<Set<JWTRecord>> opt = jwtwm.getRecords(email1);
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
        Set<JWTRecord> firstRecords = jwtwm.getRecords(email1).get();
        jwtwm.add(email2, record2);
        jwtwm.add(email1, record3);

        Set<JWTRecord> records = jwtwm.getRecords(email1).get();

        Assertions.assertEquals(records, firstRecords);

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
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        jwtwm.add(email1, record1);
        jwtwm.add(email2, record2);
        jwtwm.add(email1, record3);

        Set<JWTRecord> records = jwtwm.getRecords(email1).get();

        Assertions.assertFalse(jwtwm.remove("fake-token").isPresent());
        Assertions.assertTrue(jwtwm.remove(token1).isPresent());
        Assertions.assertTrue(records.size() == 1);
        Assertions.assertTrue(jwtwm.remove(token3).isPresent());
        Assertions.assertTrue(records.isEmpty());
        Assertions.assertFalse(jwtwm.getMap().containsKey(email1));
        Assertions.assertFalse(jwtwm.remove(token1).isPresent());

        Assertions.assertTrue(jwtwm.remove(token2).isPresent());
        Assertions.assertFalse(jwtwm.getMap().containsKey(email2));
        Assertions.assertTrue(jwtwm.getMap().isEmpty());
    }

    @Test
    public void testRemoveAll() throws Exception {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        jwtwm.add(email1, record1);
        jwtwm.add(email1, record2);
        jwtwm.add(email1, record3);

        Set<JWTRecord> records = jwtwm.getRecords(email1).get();

        Assertions.assertEquals(records.size(), 3);

        Optional<Set<JWTRecord>> opt = jwtwm.removeAll(email1);
        Assertions.assertTrue(opt.isPresent());
        Assertions.assertEquals(records, opt.get());
        Assertions.assertTrue(records.containsAll(opt.get()));
    }

    @Test
    public void testCleanUp() {

        Instant iat = now.minus(SHORT_TERM_VALIDITY * 3,
                SHORT_TERM_VALIDITY_UNIT);

        JWTRecord record1 = new JWTRecord(token1,
                Date.from(iat),
                Date.from(iat.plus(SHORT_TERM_VALIDITY,
                        SHORT_TERM_VALIDITY_UNIT)));

        iat = now.minus(SHORT_TERM_VALIDITY * 2,
                SHORT_TERM_VALIDITY_UNIT);

        JWTRecord record2 = new JWTRecord(token2,
                Date.from(iat),
                Date.from(iat.plus(SHORT_TERM_VALIDITY,
                        SHORT_TERM_VALIDITY_UNIT)));

        // future token
        JWTRecord record3 = new JWTRecord(token3,
                creationDate3,
                expirationDate3);

        final int max = 10;

        IntStream.rangeClosed(1, max)
                .parallel()
                .forEach(i -> jwtwm.add("email" + i, record1));
        jwtwm.add(email2, record2);
        jwtwm.add(email1, record3);

        Assertions.assertTrue(jwtwm.getRecords(email1).isPresent());
        Assertions.assertTrue(jwtwm.getRecords(email2).isPresent());
        Assertions.assertTrue(jwtwm.getRecords("email" + max).isPresent());

        Assertions.assertDoesNotThrow(() -> jwtwm.cleanUp());

        // record3 of email1 should remain...
        Assertions.assertTrue(jwtwm.getRecords(email1).isPresent());
        // no more records
        Assertions.assertFalse(jwtwm.getRecords(email2).isPresent());
        // no more records
        Assertions.assertFalse(jwtwm.getRecords("email" + max).isPresent());

        Assertions.assertFalse(jwtwm.getRecords(email1).get().contains(record1));
        Assertions.assertTrue(jwtwm.getRecords(email1).get().contains(record3));

        System.out.println(jwtwm);
    }

}
