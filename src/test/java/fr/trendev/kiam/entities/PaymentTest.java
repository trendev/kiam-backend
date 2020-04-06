/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.payment.entities.Payment;
import fr.trendev.kiam.paymentmode.entities.PaymentMode;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PaymentTest {

    public PaymentTest() {
    }

    /**
     * Test of getId method, of class Payment.
     */
    @Test
    public void testConstructors() {
        Payment instance = new Payment();

        assert instance.getId() == null;
        assert instance.getAmount() == 0;
        assert instance.getPaymentMode() == null;

        int amount = 1000; // 10 USD
        String currency = "USD";

        instance = new Payment(amount, new PaymentMode());

        assert instance.getId() == null;
        assert instance.getAmount() == amount;
        assert instance.getPaymentMode() != null;

    }

}
