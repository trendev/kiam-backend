package fr.trendev.kiam.paymentmode.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.paymentmode.entities.PaymentMode;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("paymentMode")
public class PaymentModeFacade extends AbstractFacade<PaymentMode, String> {

    @Inject
    private EntityManager em;

    public PaymentModeFacade() {
        super(PaymentMode.class);
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
