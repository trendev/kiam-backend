/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.productrecord.entities.ProductRecord;
import fr.trendev.kiam.productrecord.entities.ProductRecordType;
import fr.trendev.kiam.useditem.entities.UsedItem;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class UsedItemTest {

    public UsedItemTest() {
    }

    @Test
    public void testConstructor() {
        ProductRecord instance = new UsedItem();

        assert instance.getId() == null;
        assert instance.getRecordDate() != null;
        assert instance.getQty() == 0;
        assert instance.getProduct() == null;
        assert ProductRecordType.USED_ITEM.equals(instance.getCltype());
    }

}
