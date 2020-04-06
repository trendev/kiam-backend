/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.productrecord.entities.ProductRecord;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ProductRecordTest {

    public ProductRecordTest() {
    }

    @Test
    public void testConstructor() {
        ProductRecord instance = new ProductRecordImpl();

        assert instance.getId() == null;
        assert instance.getCltype() == null;
        assert instance.getRecordDate() != null;
        assert instance.getQty() == 0;
        assert instance.getProduct() == null;
        assert instance.isCancelled() == false;
        assert instance.getCancellationDate() == null;
    }

    public class ProductRecordImpl extends ProductRecord {
    }

}
