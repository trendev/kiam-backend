/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import org.junit.Test;

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
        assert instance.getPrice() == 0;
        assert instance.getDuration() == 0;
        assert instance.getProfessional() == null;
        assert instance.isHidden() == false;
        assert instance.getProfessional() == null;
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty() == true;
        assert instance.getPurchasedOfferings() != null;
        assert instance.getPurchasedOfferings().isEmpty() == true;
        assert instance.getParentPacks() != null;
        assert instance.getParentPacks().isEmpty() == true;

        String name = "Offering #1";
        int price = 1000; // 10 euros
        int duration = 60;

        instance = new OfferingImpl(name, price, duration, new Professional());

        instance.setHidden(true);

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getPrice() == price;
        assert instance.getDuration() == duration;
        assert instance.getProfessional() != null;
        assert instance.isHidden() == true;
        assert instance.getProfessional() != null;
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty() == true;
        assert instance.getPurchasedOfferings() != null;
        assert instance.getPurchasedOfferings().isEmpty() == true;
        assert instance.getParentPacks() != null;
        assert instance.getParentPacks().isEmpty() == true;

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
