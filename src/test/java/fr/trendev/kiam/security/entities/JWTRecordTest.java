/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.entities;

import fr.trendev.kiam.security.entities.JWTRecord;
import java.time.Instant;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY_UNIT;

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
    public void testConstructor() {
        Date creationDate1 = Date.from(now);
        Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JWTRecord(null, creationDate1, expirationDate1));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JWTRecord("", creationDate1, expirationDate1));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JWTRecord(token, null, Date.from(now)));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JWTRecord(token, Date.from(now), null));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JWTRecord(token, null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JWTRecord(token, Date.from(now), Date.from(now)));
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                new JWTRecord(token, expirationDate1, creationDate1));
    }
    
    @Test
    public void testEquals() {
        Date creationDate1 = Date.from(now);
        Date creationDate2 = Date.from(now);
        Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        Date expirationDate2 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        
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
        Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        Date expirationDate2 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        
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
    
    @Test
    public void testCompareTo() {
        
        String token1 = "token1";
        String token2 = "token2";
        Instant now = Instant.now();
        
        Date creationDate1 = Date.from(now);
        Date creationDate2 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));
        Date expirationDate2 = Date.from(now.plus(SHORT_TERM_VALIDITY * 2,
                SHORT_TERM_VALIDITY_UNIT));
        
        Assertions.assertTrue(
                new JWTRecord(token1, creationDate1, expirationDate1)
                        .compareTo(
                                new JWTRecord(token1, creationDate1,
                                        expirationDate1))
                == 0);
        
        Assertions.assertTrue(
                new JWTRecord(token1, creationDate1, expirationDate1)
                        .compareTo(
                                new JWTRecord(token1, creationDate1,
                                        expirationDate2))
                < 0);
        
        Assertions.assertTrue(
                new JWTRecord(token1, creationDate1, expirationDate1)
                        .compareTo(
                                new JWTRecord(token1, creationDate2,
                                        expirationDate2))
                < 0);
        
        Assertions.assertTrue(
                new JWTRecord(token1, creationDate1, expirationDate1)
                        .compareTo(
                                new JWTRecord(token2, creationDate2,
                                        expirationDate2))
                < 0);
        
    }
    
    @Test
    public void testHasExpired() {
        JWTRecord record = new JWTRecord(token,
                Date.from(now.minus(SHORT_TERM_VALIDITY * 2,
                        SHORT_TERM_VALIDITY_UNIT)),
                Date.from(now.minus(SHORT_TERM_VALIDITY,
                        SHORT_TERM_VALIDITY_UNIT)));
        Assertions.assertTrue(record.hasExpired());
        
        record = new JWTRecord(token,
                Date.from(now),
                Date.from(now.plus(SHORT_TERM_VALIDITY, SHORT_TERM_VALIDITY_UNIT)));
        
        Assertions.assertFalse(record.hasExpired());
    }
    
}
