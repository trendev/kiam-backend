/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.offering.entities.OfferingType;
import fr.trendev.kiam.pack.entities.Pack;
import fr.trendev.kiam.professional.entities.Professional;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PackTest {

    public PackTest() {
    }

    @Test
    public void testConstructors() {
        Pack instance = new Pack();

        assert instance.getId() == null;
        assert instance.getName() == null;
        assert instance.getPrice() == 0;
        assert instance.getDuration() == 0;
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();
        assert instance.getProfessional() == null;
        assert OfferingType.PACK.equals(instance.getCltype());

        String name = "Service Set #1";
        int price = 10000; // 100 euros
        int duration = 60;

        instance = new Pack(name, price, duration, new Professional());

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getPrice() == price;
        assert instance.getDuration() == duration;
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();
        assert instance.getProfessional() != null;
        assert OfferingType.PACK.equals(instance.getCltype());

    }

}
