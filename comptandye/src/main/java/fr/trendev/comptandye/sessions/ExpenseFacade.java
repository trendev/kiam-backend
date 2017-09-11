package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.ExpensePK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("expense")
public class ExpenseFacade extends AbstractFacade<Expense, ExpensePK> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ExpenseFacade() {
        super(Expense.class);
    }

}
