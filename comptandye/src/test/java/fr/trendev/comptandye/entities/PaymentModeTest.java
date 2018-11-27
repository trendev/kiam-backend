/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.paymentmode.entities.PaymentMode;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PaymentModeTest {

    public PaymentModeTest() {
    }

    @Test
    public void testConstructors() {
        PaymentMode instance = new PaymentMode();

        assert instance.getName() == null;

        String name = "Cash";

        instance = new PaymentMode(name);

        assert instance.getName().equals(name);

    }

}
