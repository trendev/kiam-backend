/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.bill.entities.BillType;
import fr.trendev.comptandye.collectivegroupbill.entities.CollectiveGroupBill;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class CollectiveGroupBillTest {

    public CollectiveGroupBillTest() {
    }

    @Test
    public void testConstructors() {
        CollectiveGroupBill instance = new CollectiveGroupBill();

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
        assert instance.getCollectiveGroup() == null;
        assert BillType.COLLECTIVEGROUP.equals(instance.getCltype());

    }

}
