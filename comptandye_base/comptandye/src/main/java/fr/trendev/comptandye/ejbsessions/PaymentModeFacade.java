package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.PaymentMode;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("paymentMode")
public class PaymentModeFacade extends AbstractFacade<PaymentMode, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaymentModeFacade() {
        super(PaymentMode.class);
    }

}