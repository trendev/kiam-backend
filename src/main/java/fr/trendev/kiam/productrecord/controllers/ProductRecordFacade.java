package fr.trendev.kiam.productrecord.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.productrecord.entities.ProductRecord;
import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class ProductRecordFacade<T extends ProductRecord> extends AbstractFacade<T, String> {

    @Inject
    private EntityManager em;

    public ProductRecordFacade(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(String pk) {
        return pk;
    }

}
