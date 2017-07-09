package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.Expense;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("expense")
public class ExpenseFacade extends AbstractFacade<Expense, Long> {

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
