package fr.trendev.comptandye.productrecord.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.productrecord.entities.ProductRecord;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("productrecord")
public class ProductRecordFacade extends AbstractFacade<ProductRecord, Long> {

    @Inject
    private EntityManager em;

    public ProductRecordFacade() {
        super(ProductRecord.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(Long pk) {
        return pk.toString();
    }

}