package fr.trendev.kiam.purchasedoffering.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.purchasedoffering.entities.PurchasedOffering;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("purchasedOffering")
public class PurchasedOfferingFacade extends AbstractFacade<PurchasedOffering, String> {

    @Inject
    private EntityManager em;

    public PurchasedOfferingFacade() {
        super(PurchasedOffering.class);
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
