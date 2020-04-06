package fr.trendev.comptandye.solditem.controllers;

import fr.trendev.comptandye.productrecord.controllers.ProductRecordFacade;
import fr.trendev.comptandye.solditem.entities.SoldItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("solditem")
public class SoldItemFacade extends ProductRecordFacade<SoldItem> {

    public SoldItemFacade() {
        super(SoldItem.class);
    }

}
