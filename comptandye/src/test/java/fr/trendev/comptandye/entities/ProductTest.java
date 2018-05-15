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
public class ProductTest {

    public ProductTest() {
    }

    @Test
    public void testConstructor() {
        Product p = new Product();
        assert p.getAvailableQty() == 0;
        assert p.getThresholdWarning() == 0;
        assert p.getThresholdSevere() == 0;
        assert p.getComments() != null;
        assert p.getComments().isEmpty() == true;
        assert p.getProfessional() == null;
        assert p.getProductReference() == null;
        assert p.getSales() != null;
        assert p.getSales().isEmpty() == true;
        assert p.getTrackRecord() != null;
        assert p.getTrackRecord().isEmpty() == true;
    }

}
