package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Payment;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("payment")
public class PaymentFacade extends AbstractFacade<Payment, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PaymentFacade() {
        super(Payment.class);
    }

}
