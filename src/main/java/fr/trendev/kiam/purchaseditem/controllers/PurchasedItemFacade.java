package fr.trendev.comptandye.purchaseditem.controllers;

import fr.trendev.comptandye.productrecord.controllers.ProductRecordFacade;
import fr.trendev.comptandye.purchaseditem.entities.PurchasedItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("purchaseditem")
public class PurchasedItemFacade extends ProductRecordFacade<PurchasedItem> {

    public PurchasedItemFacade() {
        super(PurchasedItem.class);
    }

}
