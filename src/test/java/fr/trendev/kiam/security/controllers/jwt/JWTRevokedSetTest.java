/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt;

import fr.trendev.kiam.security.controllers.jwt.JWTRevokedSet;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY_UNIT;
import fr.trendev.kiam.security.controllers.jwt.dto.mock.MockJWTRevokedSetDTO;
import fr.trendev.kiam.security.entities.JWTRecord;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;
import javax.inject.Inject;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class JWTRevokedSetTest {

    private final String token1 = "token1";
    private final String token2 = "token2";
    private final String token3 = "token3";
    private final Instant now = Instant.now();

    private final Date creationDate1 = Date.from(now);
    private final Date creationDate2 = Date.from(now.plus(5,
            SHORT_TERM_VALIDITY_UNIT));
    private final Date creationDate3 = Date.from(now.plus(10,
            SHORT_TERM_VALIDITY_UNIT));
    private final Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
            SHORT_TERM_VALIDITY_UNIT));
    private final Date expirationDate2 = Date.from(now.plus(SHORT_TERM_VALIDITY
            + 5,
            SHORT_TERM_VALIDITY_UNIT));
    private final Date expirationDate3 = Date.from(now.plus(SHORT_TERM_VALIDITY
            + 10,
            SHORT_TERM_VALIDITY_UNIT));

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(JWTRevokedSet.class, MockJWTRevokedSetDTO.class)
            .inject(this).build();

    @Inject
    JWTRevokedSet jwtrvkset;

    public JWTRevokedSetTest() {
    }

    @Before
    public void init() {
        if (jwtrvkset != null) {
            jwtrvkset.clear();
        }
    }

    @After
    public void clear() {
        jwtrvkset.clear();
    }

    @Test
    public void testInjection() {
        Assertions.assertNotNull(jwtrvkset);
        Assertions.assertDoesNotThrow(() -> jwtrvkset.init());
    }

    @Test
    public void testInit() {
    }

    @Test
    public void testClose() {
    }

    @Test
    public void testGetSet() {
        Assertions.assertNotNull(jwtrvkset.getSet());
        Assertions.assertTrue(jwtrvkset.getSet().isEmpty());
    }

    @Test
    public void testAdd() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Assertions.assertTrue(jwtrvkset.add(record1));
        Assertions.assertEquals(jwtrvkset.getSet().size(), 1);
        Assertions.assertTrue(jwtrvkset.add(record2));
        Assertions.assertEquals(jwtrvkset.getSet().size(), 2);
        Assertions.assertTrue(jwtrvkset.add(record3));
        Assertions.assertEquals(jwtrvkset.getSet().size(), 3);

        //control duplication
        Assertions.assertFalse(jwtrvkset.add(record1));
        Assertions.assertEquals(jwtrvkset.getSet().size(), 3);
    }

    @Test
    public void testContains() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Assertions.assertTrue(jwtrvkset
                .addAll(new HashSet<>(Arrays.asList(record1, record2, record3))));

        Assertions.assertTrue(jwtrvkset.contains(token1));
        Assertions.assertTrue(jwtrvkset.contains(token2));
        Assertions.assertTrue(jwtrvkset.contains(token3));

        Assertions.assertFalse(jwtrvkset.contains("fake-token"));

    }

    @Test
    public void testRemove_String() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Assertions.assertTrue(jwtrvkset
                .addAll(new HashSet<>(Arrays.asList(record1, record2, record3))));

        Assertions.assertTrue(jwtrvkset.remove(token1).isPresent());
        Assertions.assertEquals(jwtrvkset.getSet().size(), 2);

        Assertions.assertFalse(jwtrvkset.remove(token1).isPresent());
        Assertions.assertEquals(jwtrvkset.getSet().size(), 2);
    }

    @Test
    public void testRemove_JWTRecord() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        Assertions.assertTrue(jwtrvkset
                .addAll(new HashSet<>(Arrays.asList(record1, record2, record3))));

        Assertions.assertTrue(jwtrvkset.remove(record1).isPresent());
        Assertions.assertEquals(jwtrvkset.getSet().size(), 2);

        Assertions.assertFalse(jwtrvkset.remove(record1).isPresent());
        Assertions.assertEquals(jwtrvkset.getSet().size(), 2);
    }

    @Test
    public void testClear() {
        jwtrvkset.clear();
        Assertions.assertTrue(jwtrvkset.getSet().isEmpty());
    }

    @Test
    public void testAddAll() {
        JWTRecord record1 = new JWTRecord(token1, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token2, creationDate2, expirationDate2);
        JWTRecord record3 = new JWTRecord(token3, creationDate3, expirationDate3);

        List<JWTRecord> records = Arrays.asList(record1, record2, record3);

        Assertions.assertTrue(jwtrvkset.addAll(new HashSet<>(records)));
        Assertions.assertEquals(jwtrvkset.getSet().size(), records.size());

        Assertions.assertFalse(jwtrvkset.addAll(new HashSet<>(records)));
        Assertions.assertEquals(jwtrvkset.getSet().size(), records.size());
    }

    @Test
    public void testCleanUp() throws Exception {

        final Instant iat = now.minus(SHORT_TERM_VALIDITY * 3,
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
                .forEach(i -> jwtrvkset.add(new JWTRecord("token_" + i,
                        Date.from(iat),
                        Date.from(iat.plus(SHORT_TERM_VALIDITY,
                                SHORT_TERM_VALIDITY_UNIT)))));
        jwtrvkset.add(record2);
        jwtrvkset.add(record3);

        Assertions.assertTrue(jwtrvkset.contains("token_1"));
        Assertions.assertTrue(jwtrvkset.contains("token_" + max));
        Assertions.assertTrue(jwtrvkset.contains(token2));
        Assertions.assertTrue(jwtrvkset.contains(token3));

        Assertions.assertDoesNotThrow(() -> jwtrvkset.cleanUp());

        Assertions.assertFalse(jwtrvkset.contains("token_1"));
        Assertions.assertFalse(jwtrvkset.contains("token_" + max));
        Assertions.assertFalse(jwtrvkset.contains(token2));
        Assertions.assertTrue(jwtrvkset.contains(token3));
        Assertions.assertEquals(jwtrvkset.getSet().size(), 1);

        System.out.println(jwtrvkset);
    }

}
