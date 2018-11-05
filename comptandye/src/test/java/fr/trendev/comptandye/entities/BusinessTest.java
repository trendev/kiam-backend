/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.business.entities.Business;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class BusinessTest {

    public BusinessTest() {
    }

    @Test
    public void testGetName() {

        Business instance = new Business();

        assert instance.getDesignation() == null;

        String name = "R&D";
        instance = new Business(name);

        assert name.equals(instance.getDesignation());

    }

}
