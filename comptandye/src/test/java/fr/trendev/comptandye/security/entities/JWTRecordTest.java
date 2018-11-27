/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.entities;

import fr.trendev.comptandye.security.controllers.JWTManager;
import java.time.Instant;
import static java.time.temporal.ChronoUnit.MINUTES;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class JWTRecordTest {

    private String token;
    private Instant now;

    @Before
    public void init() {
        token = "TK123456789";
        now = Instant.now();
    }

    @Test
    public void testEquals() {
        Date creationDate1 = Date.from(now);
        Date creationDate2 = Date.from(now);
        Date expirationDate1 = Date.from(now.plus(JWTManager.VALID_PERIOD,
                MINUTES));
        Date expirationDate2 = Date.from(now.plus(JWTManager.VALID_PERIOD,
                MINUTES));

        JWTRecord record1 = new JWTRecord(token, creationDate1, expirationDate1);
        JWTRecord record1b = new JWTRecord(token, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token, creationDate2, expirationDate2);

        Assertions.assertEquals(record1, record1b);
        Assertions.assertEquals(record1, record2);
        Assertions.assertEquals(new JWTRecord("TK123456789", creationDate1,
                expirationDate1), record1);
        Assertions.assertNotEquals(
                new JWTRecord("NEWTK123456789", creationDate1,
                        expirationDate1), record1);

    }

    @Test
    public void testHashCode() {
        Date creationDate1 = Date.from(now);
        Date creationDate2 = Date.from(now);
        Date expirationDate1 = Date.from(now.plus(JWTManager.VALID_PERIOD,
                MINUTES));
        Date expirationDate2 = Date.from(now.plus(JWTManager.VALID_PERIOD,
                MINUTES));

        JWTRecord record1 = new JWTRecord(token, creationDate1, expirationDate1);
        JWTRecord record1b = new JWTRecord(token, creationDate1, expirationDate1);
        JWTRecord record2 = new JWTRecord(token, creationDate2, expirationDate2);

        Assertions.assertEquals(record1.hashCode(), record1b.hashCode());
        Assertions.assertEquals(record1.hashCode(), record2.hashCode());
        Assertions.assertEquals(new JWTRecord("TK123456789", creationDate1,
                expirationDate1).hashCode(), record1.hashCode());
        Assertions.assertNotEquals(
                new JWTRecord("NEWTK123456789", creationDate1,
                        expirationDate1).hashCode(), record1.hashCode());

    }

}
