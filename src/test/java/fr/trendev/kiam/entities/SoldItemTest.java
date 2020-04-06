/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.productrecord.entities.ProductRecord;
import fr.trendev.kiam.productrecord.entities.ProductRecordType;
import fr.trendev.kiam.solditem.entities.SoldItem;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class SoldItemTest {

    public SoldItemTest() {
    }

    @Test
    public void testConstructor() {
        ProductRecord instance = new SoldItem();

        assert instance.getId() == null;
        assert instance.getRecordDate() != null;
        assert instance.getQty() == 0;
        assert instance.getProduct() == null;
        assert ProductRecordType.SOLD_ITEM.equals(instance.getCltype());
    }

}
