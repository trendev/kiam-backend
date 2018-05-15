package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.ExpenseItem;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("expenseitem")
public class ExpenseItemFacade extends AbstractFacade<ExpenseItem, Long> {

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
    public String prettyPrintPK(Long pk) {
        return pk.toString();
    }

}
