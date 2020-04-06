package fr.trendev.kiam.purchaseditem.controllers;

import fr.trendev.kiam.productrecord.controllers.ProductRecordFacade;
import fr.trendev.kiam.purchaseditem.entities.PurchasedItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("purchaseditem")
public class PurchasedItemFacade extends ProductRecordFacade<PurchasedItem> {

    public PurchasedItemFacade() {
        super(PurchasedItem.class);
    }

}
