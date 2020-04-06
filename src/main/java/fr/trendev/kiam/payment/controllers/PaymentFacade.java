package fr.trendev.kiam.payment.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.payment.entities.Payment;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("payment")
public class PaymentFacade extends AbstractFacade<Payment, String> {

    @Inject
    private EntityManager em;

    public PaymentFacade() {
        super(Payment.class);
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
