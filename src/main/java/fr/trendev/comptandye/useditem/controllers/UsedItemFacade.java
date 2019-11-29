package fr.trendev.comptandye.useditem.controllers;

import fr.trendev.comptandye.productrecord.controllers.ProductRecordFacade;
import fr.trendev.comptandye.useditem.entities.UsedItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("useditem")
public class UsedItemFacade extends ProductRecordFacade<UsedItem> {

    public UsedItemFacade() {
        super(UsedItem.class);
    }
}
