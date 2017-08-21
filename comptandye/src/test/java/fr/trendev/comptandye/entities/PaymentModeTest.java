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
public class PaymentModeTest {

    public PaymentModeTest() {
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
        PaymentMode instance = new PaymentMode();

        assert instance.getId() == null;
        assert instance.getName() == null;

        String name = "Cash";

        instance = new PaymentMode(name);

        assert instance.getId() == null;
        assert instance.getName().equals(name);

    }

}
