package fr.trendev.comptandye.classicexpense.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.classicexpense.entities.ClassicExpense;
import fr.trendev.comptandye.expense.entities.ExpensePK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("classicExpense")
public class ClassicExpenseFacade extends AbstractFacade<ClassicExpense, ExpensePK> {

    @Inject
    private EntityManager em;

    public ClassicExpenseFacade() {
        super(ClassicExpense.class);
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
