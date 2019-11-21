/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.useraccount.controllers.UUIDGenerator;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 *
 * @author jsie
 */
public class UUIDGeneratorTest {
    
    private UUIDGenerator UUIDGenerator;

    public UUIDGeneratorTest() {
         this.UUIDGenerator = new UUIDGenerator();
    }

    /**
     * Test of generate method, of class UUIDGenerator.
     */
    @Test
    public void testGenerate() {
        String s1 = UUIDGenerator.generate();
        String s2 = UUIDGenerator.generate();
        assertNotEquals(s1, s2);

    }

    /**
     * Test of generate method, of class UUIDGenerator.
     */
    @Test
    public void testGenerate_boolean() {
        boolean compact = true;
        String s1 = UUIDGenerator.generate(compact);
        String s2 = UUIDGenerator.generate(compact);
        assertNotEquals(s1, s2);
    }

    /**
     * Test of generate method, of class UUIDGenerator.
     */
    @Test
    public void testGenerate_String_boolean() {
        String header = null;
        boolean compact = true;
        String s1 = UUIDGenerator.generate(header, compact);
        String s2 = UUIDGenerator.generate(header, compact);
        assertNotEquals(s1, s2);

    }

}
