package fr.trendev.comptandye.returneditem.controllers;

import fr.trendev.comptandye.productrecord.controllers.ProductRecordFacade;
import fr.trendev.comptandye.returneditem.entities.ReturnedItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("returneditem")
public class ReturnedItemFacade extends ProductRecordFacade<ReturnedItem> {

    public ReturnedItemFacade() {
        super(ReturnedItem.class);
    }
}
