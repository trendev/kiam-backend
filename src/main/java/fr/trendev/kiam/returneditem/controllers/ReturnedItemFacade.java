package fr.trendev.kiam.returneditem.controllers;

import fr.trendev.kiam.productrecord.controllers.ProductRecordFacade;
import fr.trendev.kiam.returneditem.entities.ReturnedItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("returneditem")
public class ReturnedItemFacade extends ProductRecordFacade<ReturnedItem> {

    public ReturnedItemFacade() {
        super(ReturnedItem.class);
    }
}
