/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.useraccount.controllers.UUIDGenerator;

/**
 *
 * @author jsie
 */
public class UUIDGeneratorTest {

    public UUIDGeneratorTest() {
    }

    /**
     * Test of generate method, of class UUIDGenerator.
     */
    //@Test
    public void testGenerate() {
        System.out.println("testGenerate()");

        String s1 = UUIDGenerator.generate();
        String s2 = UUIDGenerator.generate();

        System.out.println("s1=" + s1 + "\ns2=" + s2);
        assert !s1.equals(s2);

    }

    /**
     * Test of generate method, of class UUIDGenerator.
     */
    //@Test
    public void testGenerate_boolean() {
        System.out.println("testGenerate_boolean()");
        boolean compact = true;
        String s1 = UUIDGenerator.generate(compact);
        String s2 = UUIDGenerator.generate(compact);

        System.out.println("s1=" + s1 + "\ns2=" + s2);
        assert !s1.equals(s2);

    }

    /**
     * Test of generate method, of class UUIDGenerator.
     */
    //@Test
    public void testGenerate_String_boolean() {
        System.out.println("testGenerate_String_boolean()");
        String header = null;
        boolean compact = true;
        String s1 = UUIDGenerator.generate(header, compact);
        String s2 = UUIDGenerator.generate(header, compact);

        System.out.println("s1=" + s1 + "\ns2=" + s2);
        assert !s1.equals(s2);

    }

}
