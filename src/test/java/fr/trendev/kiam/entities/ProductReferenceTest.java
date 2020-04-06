/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.productreference.entities.ProductReference;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ProductReferenceTest {

    public ProductReferenceTest() {
    }

    @Test
    public void testConstructor() {
        ProductReference pr = new ProductReference();
        assert pr.getBarcode() == null;
        assert pr.getDescription() == null;
        assert pr.getBrand() == null;
        assert pr.getBusiness() == null;
    }

}
