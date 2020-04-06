package fr.trendev.kiam.solditem.controllers;

import fr.trendev.kiam.productrecord.controllers.ProductRecordFacade;
import fr.trendev.kiam.solditem.entities.SoldItem;
import javax.ejb.Stateless;
import javax.inject.Named;

@Stateless
@Named("solditem")
public class SoldItemFacade extends ProductRecordFacade<SoldItem> {

    public SoldItemFacade() {
        super(SoldItem.class);
    }

}
