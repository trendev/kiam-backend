/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.productrecord.entities.ProductRecord;
import fr.trendev.comptandye.productrecord.entities.ProductRecordType;
import fr.trendev.comptandye.returneditem.entities.ReturnedItem;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ReturnedItemTest {

    public ReturnedItemTest() {
    }

    @Test
    public void testConstructor() {
        ProductRecord instance = new ReturnedItem();

        assert instance.getId() == null;
        assert instance.getRecordDate() != null;
        assert instance.getQty() == 0;
        assert instance.getProduct() == null;
        assert ProductRecordType.RETURNED_ITEM.equals(instance.getCltype());
    }

}
