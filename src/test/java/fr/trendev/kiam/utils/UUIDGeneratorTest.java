/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.utils;

import fr.trendev.kiam.utils.UUIDGenerator;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertFalse(s1.contains("-"));
        assertFalse(s2.contains("-"));
    }

    /**
     * Test of generate method, of class UUIDGenerator.
     */
    @Test
    public void testGenerate_String_boolean() {
        String header = "HEADER";
        boolean compact = true;
        String s1 = UUIDGenerator.generate(header, compact);
        String s2 = UUIDGenerator.generate(header, compact);
        assertNotEquals(s1, s2);
        assertFalse(s1.contains("-"));
        assertFalse(s2.contains("-"));
        assertTrue(s1.contains(header));
        assertTrue(s2.contains(header));
    }

    /**
     * Test ID Generator
     */
    @Test
    public void testGenerateID() {

        int SIZE = 30;

        String[] ids = new String[SIZE];

        for (int i = 0; i < SIZE; i++) {
            ids[i] = UUIDGenerator.generateID();
            String id = ids[i];
            assertFalse(id.contains("-"));
            assertEquals(id.length(), 32);
        }

        Arrays.asList(ids)
                .stream()
                .sorted()
                .forEach(System.out::println);

    }

}
