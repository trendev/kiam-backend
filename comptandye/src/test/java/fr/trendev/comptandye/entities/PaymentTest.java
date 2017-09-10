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
public class PaymentTest {

    public PaymentTest() {
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

    /**
     * Test of getId method, of class Payment.
     */
    @Test
    public void testConstructors() {
        Payment instance = new Payment();

        assert instance.getId() == null;
        assert instance.getAmount() == 0;
        assert instance.getCurrency().equals("EUR");
        assert instance.getPaymentMode() == null;

        int amount = 1000; // 10 USD
        String currency = "USD";

        instance = new Payment(amount, currency, new PaymentMode());

        assert instance.getId() == null;
        assert instance.getAmount() == amount;
        assert instance.getCurrency().equals(currency);
        assert instance.getPaymentMode() != null;

    }

}
