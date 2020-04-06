package fr.trendev.kiam.useditem.controllers;

import fr.trendev.kiam.productrecord.controllers.ProductRecordFacade;
import fr.trendev.kiam.useditem.entities.UsedItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("useditem")
public class UsedItemFacade extends ProductRecordFacade<UsedItem> {

    public UsedItemFacade() {
        super(UsedItem.class);
    }
}
