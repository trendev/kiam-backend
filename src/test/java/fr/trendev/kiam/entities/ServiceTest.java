/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.offering.entities.OfferingType;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.service.entities.Service;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ServiceTest {

    public ServiceTest() {
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
