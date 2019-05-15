package fr.trendev.comptandye.purchaseexpense.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.expense.entities.ExpensePK;
import fr.trendev.comptandye.purchaseexpense.entities.PurchaseExpense;
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
