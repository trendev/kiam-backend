/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.utils.OfferingType;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PurchasedOfferingTest {

    public PurchasedOfferingTest() {
    }

    @Test
    public void testConstructor() {
        PurchasedOffering po = new PurchasedOffering();

        assert po.getId() == null;
        assert po.getQty() == 1;
        assert po.getOffering() == null;

        Service service = new Service("test_service", 1000, 120,
                new Professional());
        service.setCltype(OfferingType.SERVICE);
        service.getBusinesses().add(new Business("business#1"));
        service.getBusinesses().add(new Business("business#2"));

        po = new PurchasedOffering(5, service);
        assert po.getId() == null;
        assert po.getQty() == 5;
        assert po.getOffering() == null;
        assert po.getOfferingSnapshot() != null;
        assert po.getOfferingSnapshot().getCltype().equals(service.getCltype());
        assert po.getOfferingSnapshot().getName().equals(service.getName());
        assert po.getOfferingSnapshot().getPrice() == service.getPrice();
        assert po.getOfferingSnapshot().getDuration() == service.getDuration();
        assert po.getOfferingSnapshot().getBusinesses() != null;
        assert po.getOfferingSnapshot().getBusinesses().size() == service.
                getBusinesses().size();
        assert po.getOfferingSnapshot().getBusinesses().containsAll(service.
                getBusinesses()) == true;

    }

}
