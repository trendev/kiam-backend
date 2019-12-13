/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.offering.entities.Offering;
import fr.trendev.comptandye.pack.entities.Pack;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.service.entities.Service;
import java.util.Set;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * @author jsie
 */
public class OfferingTest {

    public OfferingTest() {
    }

    /**
     * Test of getId method, of class Offering.
     */
    @Test
    public void testConstructors() {
        Offering instance = new OfferingImpl();

        assert instance.getId() == null;
        assert instance.getName() == null;
        assert instance.getShortname() == null;
        assert instance.getPrice() == 0;
        assert instance.getDuration() == 0;
        assert instance.getProfessional() == null;
        assert instance.getProfessional() == null;
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty() == true;
        assert instance.getPurchasedOfferings() != null;
        assert instance.getPurchasedOfferings().isEmpty() == true;
        assert instance.getParentPacks() != null;
        assert instance.getParentPacks().isEmpty() == true;
        assert instance.getCltype() == null;

        String name = "Offering #1";
        int price = 1000; // 10 euros
        int duration = 60;

        instance = new OfferingImpl(name, price, duration, new Professional());

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getShortname() == null;
        assert instance.getPrice() == price;
        assert instance.getDuration() == duration;
        assert instance.getProfessional() != null;
        assert instance.getProfessional() != null;
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty() == true;
        assert instance.getPurchasedOfferings() != null;
        assert instance.getPurchasedOfferings().isEmpty() == true;
        assert instance.getParentPacks() != null;
        assert instance.getParentPacks().isEmpty() == true;
        assert instance.getCltype() == null;

    }

    @Test
    public void testGetAllParentPacks() {
        Pack p1 = new Pack();
        p1.setId("p1");

        Service s1 = new Service();
        s1.setId("s1");

        Pack p2 = new Pack();
        p2.setId("p2");

        Service s2 = new Service();
        s2.setId("s2");

        Pack p3 = new Pack();
        p3.setId("p3");

        Service s3 = new Service();
        s3.setId("s3");

        Pack p4 = new Pack();
        p4.setId("p4");

        Service s4 = new Service();
        s4.setId("s4");

        Service s5 = new Service();
        s5.setId("s5");

        p1.getOfferings().add(s1);
        s1.getParentPacks().add(p1);

        p1.getOfferings().add(p2);
        p2.getParentPacks().add(p1);

        p2.getOfferings().add(s2);
        s2.getParentPacks().add(p2);

        p2.getOfferings().add(p3);
        p3.getParentPacks().add(p2);

        p3.getOfferings().add(s3);
        s3.getParentPacks().add(p3);

        p4.getOfferings().add(s4);
        s4.getParentPacks().add(p4);
        p4.getOfferings().add(s5);
        s5.getParentPacks().add(p4);
        p4.getOfferings().add(p3);
        p3.getParentPacks().add(p4);
        p4.getOfferings().add(p2);
        p2.getParentPacks().add(p4);

        Set<Offering> _p3 = p3.getAllParentPacks();
        assertNotNull(_p3);
        assertFalse(_p3.isEmpty());
        assertTrue(_p3.contains(p1));
        assertTrue(_p3.stream().anyMatch(p -> p.getId().equals("p1")));
        assertTrue(_p3.contains(p2));
        assertTrue(_p3.stream().anyMatch(p -> p.getId().equals("p2")));
        assertTrue(_p3.contains(p4));
        assertTrue(_p3.stream().anyMatch(p -> p.getId().equals("p4")));
        assertFalse(_p3.contains(s4));
        assertFalse(_p3.contains(s5));
        assertFalse(_p3.contains(s2));
        assertFalse(_p3.contains(s1));

        Set<Offering> _p2 = p2.getAllParentPacks();
        assertNotNull(_p2);
        assertFalse(_p2.isEmpty());
        assertTrue(_p2.contains(p1));
        assertTrue(_p2.stream().anyMatch(p -> p.getId().equals("p1")));
        assertTrue(_p2.contains(p4));
        assertTrue(_p2.stream().anyMatch(p -> p.getId().equals("p4")));
        assertFalse(_p2.contains(p2));
        assertFalse(_p2.contains(p3));
        assertFalse(_p2.contains(s4));
        assertFalse(_p2.contains(s5));
        assertFalse(_p2.contains(s1));
    }

    public class OfferingImpl extends Offering {

        public OfferingImpl(String name, int price, int duration,
                Professional professionalFromOffering) {
            super(name, price, duration, professionalFromOffering);
        }

        public OfferingImpl() {
        }

    }

}
