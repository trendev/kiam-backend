package fr.trendev.comptandye.purchasedoffering.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.purchasedoffering.entities.PurchasedOffering;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("purchasedOffering")
public class PurchasedOfferingFacade extends AbstractFacade<PurchasedOffering, Long> {

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
    public String prettyPrintPK(Long pk) {
        return pk.toString();
    }

}
