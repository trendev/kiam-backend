/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class OfferingTest {

    public OfferingTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
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

    }

    @Test
    public void testBusinessesGetter() {
        Offering instance = new OfferingImpl();
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty() == true;
    }

    @Test
    public void testBusinessesSetter() {
        Offering instance = new OfferingImpl();
        instance.setBusinesses(Arrays.asList(new Business(), new Business(),
                new Business()));
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().size() == 3;
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
