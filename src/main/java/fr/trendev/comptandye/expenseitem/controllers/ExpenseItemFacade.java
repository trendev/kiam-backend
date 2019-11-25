package fr.trendev.comptandye.expenseitem.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.expenseitem.entities.ExpenseItem;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("expenseitem")
public class ExpenseItemFacade extends AbstractFacade<ExpenseItem, String> {

    @Inject
    private EntityManager em;

    public ExpenseItemFacade() {
        super(ExpenseItem.class);
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
