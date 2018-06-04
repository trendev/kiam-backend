package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.entities.PurchaseExpense;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("purchaseExpense")
public class PurchaseExpenseFacade extends AbstractFacade<PurchaseExpense, ExpensePK> {

    @Inject
    private EntityManager em;

    public PurchaseExpenseFacade() {
        super(PurchaseExpense.class);
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