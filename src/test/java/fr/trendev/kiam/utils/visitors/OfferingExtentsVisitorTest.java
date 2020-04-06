/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.utils.visitors;

import fr.trendev.kiam.offering.entities.OfferingExtentsVisitor;
import fr.trendev.kiam.pack.entities.Pack;
import fr.trendev.kiam.purchasedoffering.entities.OfferingExtents;
import fr.trendev.kiam.sale.entities.Sale;
import fr.trendev.kiam.service.entities.Service;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class OfferingExtentsVisitorTest {

    public OfferingExtentsVisitorTest() {
    }

    @Test
    public void testVisit_Offering() {

        Pack p1 = new Pack();
        p1.setName("p1");
        p1.setPrice(11000);

        Service sv1 = new Service();
        sv1.setName("sv1");
        sv1.setPrice(3000);

        Sale sl1 = new Sale();
        sl1.setName("sl1");
        sl1.setPrice(7000);

        Pack p2 = new Pack();
        p2.setName("p2");
        p2.setPrice(2750);

        Service sv2 = new Service();
        sv2.setName("sv2");
        sv2.setPrice(5412);

        Sale sl2 = new Sale();
        sl2.setName("sl2");
        sl2.setPrice(2080);

        p2.getOfferings().add(sv2);
        p2.getOfferings().add(sl2);
        p1.getOfferings().add(sv1);
        p1.getOfferings().add(sl1);
        p1.getOfferings().add(p2);

        // Test using the visitor directly
        OfferingExtents oa = p1.accept(new OfferingExtentsVisitor());
        // Test using the OfferingExtents constructor
        OfferingExtents oa2 = new OfferingExtents(p1);

        // control the services extents value
        assert oa.getServices() == oa2.getServices();
        // control the sales extents value
        assert oa.getSales() == oa2.getSales();

    }

}
