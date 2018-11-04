/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.purchaseditem.entities.PurchasedItem;
import fr.trendev.comptandye.productrecord.entities.ProductRecord;
import fr.trendev.comptandye.productrecord.entities.ProductRecordType;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class PurchasedItemTest {

    public PurchasedItemTest() {
    }

    @Test
    public void testConstructor() {
        ProductRecord instance = new PurchasedItem();

        assert instance.getId() == null;
        assert instance.getRecordDate() != null;
        assert instance.getQty() == 0;
        assert instance.getProduct() == null;
        assert ProductRecordType.PURCHASED_ITEM.equals(instance.getCltype());
    }

}
