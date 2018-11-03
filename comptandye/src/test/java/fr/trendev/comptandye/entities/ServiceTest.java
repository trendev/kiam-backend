/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.service.entities.Service;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.utils.OfferingType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ServiceTest {

    public ServiceTest() {
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
     * Test of getDuration method, of class Service.
     */
    @Test
    public void testGetDuration() {
        Service instance = new Service();

        assert instance.getId() == null;
        assert instance.getName() == null;
        assert instance.getPrice() == 0;
        assert instance.getDuration() == 0;
        assert instance.getProfessional() == null;
        assert OfferingType.SERVICE.equals(instance.getCltype());

        String name = "Service #1";
        int price = 1000; // 10 euros
        int duration = 60; // 1 hour

        instance = new Service(name, price, duration, new Professional());

        assert instance.getId() == null;
        assert instance.getName().equals(name);
        assert instance.getPrice() == price;
        assert instance.getDuration() == duration;
        assert instance.getProfessional() != null;
        assert OfferingType.SERVICE.equals(instance.getCltype());
    }

}
