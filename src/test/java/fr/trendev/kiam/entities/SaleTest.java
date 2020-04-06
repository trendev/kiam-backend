/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.offering.entities.OfferingType;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.sale.entities.Sale;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class SaleTest {

    public SaleTest() {
    }

    /**
     * Test of getDuration method, of class Sale.
     */
    @Test
    public void testGetDuration() {
        Sale instance = new Sale();

        assert instance.getId() == null;
        assert instance.getName() == null;
        assert instance.getPrice() == 0;
        assert instance.getDuration() == 0;
        assert instance.getProfessional() == null;
        assert instance.getQty() == 0;
        assert OfferingType.SALE.equals(instance.getCltype());

        String name = "Sale #1";
        int price = 1000; // 10 euros
        int duration = 60; // 1 hour
        int qty = 5;

        instance = new Sale(name, price, duration, new Professional());
        instance.setQty(qty);

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getPrice() == price;
        assert instance.getDuration() == duration;
        assert instance.getProfessional() != null;
        assert instance.getQty() == qty;
        assert OfferingType.SALE.equals(instance.getCltype());

    }

}
