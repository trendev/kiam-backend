package fr.trendev.kiam.basicexpense.controllers;

import fr.trendev.kiam.basicexpense.entities.BasicExpense;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.expense.entities.ExpensePK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("basicExpense")
public class BasicExpenseFacade extends AbstractFacade<BasicExpense, ExpensePK> {

    @Inject
    private EntityManager em;

    public BasicExpenseFacade() {
        super(BasicExpense.class);
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
