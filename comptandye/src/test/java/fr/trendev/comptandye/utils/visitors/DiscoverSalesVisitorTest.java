/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.visitors;

import fr.trendev.comptandye.offering.entities.DiscoverSalesVisitor;
import fr.trendev.comptandye.pack.entities.Pack;
import fr.trendev.comptandye.sale.entities.Sale;
import fr.trendev.comptandye.service.entities.Service;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class DiscoverSalesVisitorTest {

    public DiscoverSalesVisitorTest() {
    }

    @Test
    public void discover() {
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

        Pack p3 = new Pack();
        p3.setName("p3");
        p3.setPrice(900);

        Sale sl3 = new Sale();
        sl3.setName("sl3");
        sl3.setPrice(1000);

        p2.getOfferings().add(sv2);
        p2.getOfferings().add(sl2);
        p3.getOfferings().add(sl3);
        p2.getOfferings().add(p3);
        p1.getOfferings().add(sv1);
        p1.getOfferings().add(sl1);
        p1.getOfferings().add(p2);

        List<Sale> sales = Arrays.asList(sl1, sl2, sl3);

        List<Sale> list = p1.accept(new DiscoverSalesVisitor());
        assert !list.isEmpty();
        assert list.size() == sales.size();
        assert list.containsAll(sales);
    }

}
