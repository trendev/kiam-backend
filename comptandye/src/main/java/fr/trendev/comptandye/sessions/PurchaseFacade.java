package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.entities.Purchase;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("purchase")
public class PurchaseFacade extends AbstractFacade<Purchase, ExpensePK> {

    @Inject
    private EntityManager em;

    public PurchaseFacade() {
        super(Purchase.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(ExpensePK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getId());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

}
