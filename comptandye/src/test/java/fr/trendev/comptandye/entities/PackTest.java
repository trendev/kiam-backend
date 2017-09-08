/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PackTest {

    public PackTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testConstructors() {
        Pack instance = new Pack();

        assert instance.getId() == null;
        assert instance.getName() == null;
        assert instance.getPrice() == 0;
        assert instance.getDuration() == 0;
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();
        assert instance.getProfessionalFromOffering() == null;

        String name = "Service Set #1";
        int price = 10000; // 100 euros
        int duration = 60;

        instance = new Pack(name, price, duration, new Professional());

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getPrice() == price;
        assert instance.getDuration() == duration;
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();
        assert instance.getProfessionalFromOffering() != null;

    }

}
