/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.bill.entities.BillType;
import fr.trendev.kiam.individualbill.entities.IndividualBill;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class InvidualBillTest {

    public InvidualBillTest() {
    }

    /**
     * Test of getIndividual method, of class IndividualBill.
     */
    @Test
    public void testConstructors() {
        IndividualBill instance = new IndividualBill();
        assert instance.getReference() == null;
        assert instance.getDeliveryDate() == null;
        assert instance.getAmount() == 0;
        assert instance.getCurrency().equals("EUR");
        assert instance.getDiscount() == 0;
        assert instance.getPaymentDate() == null;
        assert instance.getComments() != null;
        assert instance.getComments().isEmpty();
        assert instance.getProfessional() == null;
        assert instance.getPayments() != null;
        assert instance.getPayments().isEmpty();
        assert instance.getPurchasedOfferings() != null;
        assert instance.getPurchasedOfferings().isEmpty();
        assert instance.getIndividual() == null;

        assert BillType.INDIVIDUAL.equals(instance.getCltype());

    }

}
