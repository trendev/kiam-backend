/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.ExpenseItem;
import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
public abstract class AbstractExpenseService<T extends Expense> extends AbstractCommonService<T, ExpensePK> {

    @Inject
    ProfessionalFacade professionalFacade;

    private final Logger LOG = Logger.getLogger(
            AbstractExpenseService.class.
                    getName());

    public AbstractExpenseService(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    public Response post(SecurityContext sec, T entity, String professional) {
        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade, T::setProfessional,
                Professional::getExpenses, e -> {
            e.setId(null);
            e.setCancelled(false);
            e.setCancellationDate(null);
            e.setIssueDate(new Date());

            if (e.getBusinesses() == null || e.getBusinesses().isEmpty()) {
                String errmsg = "Businesses in " + entity.getClass().
                        getSimpleName() + " must be provided";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (e.getAmount() <= 0) {
                String errmsg = "Amount in " + entity.getClass().getSimpleName()
                        + " must be greater than 0";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (e.getPayments() == null || e.getPayments().isEmpty()) {
                String errmsg = "Payments in " + entity.getClass().
                        getSimpleName() + " must be provided";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            int total = e.getPayments().stream()
                    .mapToInt(Payment::getAmount)
                    .sum();

            if (total != e.getAmount()) {
                String errmsg = "Amount is " + e.getAmount()
                        + " but the total amount computed is " + total;
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (e.isVatInclusive() && (e.getExpenseItems() == null || e.
                    getExpenseItems().isEmpty())) {
                LOG.log(Level.INFO,
                        "vatInclusive field is reset to false in {0}: no ExpenseItem found",
                        entity.
                                getClass().getSimpleName());
                e.setVatInclusive(false);
            }

            /**
             * Checks accuracy of the provided amount and the total computed
             * amount from the ExpenseItems. Total computed amount from the
             * ExpenseItemsProvided amount should be between
             */
            if (e.getExpenseItems() != null && !e.getExpenseItems().isEmpty()) {
                e.setVatInclusive(true);
                // computes how many items are found in this Expense 
                int n = e.getExpenseItems().stream()
                        .mapToInt(ExpenseItem::getQty)
                        .sum();
                LOG.log(Level.INFO, "Checking ExpenseItems... n = {0}",
                        n);

                total = e.getExpenseItems().stream()
                        .mapToInt(ei ->
                                ei.getQty() * new BigDecimal(ei.getAmount()).
                                multiply(ei.getVatRate()
                                        .add(new BigDecimal(100)))
                                .divide(new BigDecimal(100))
                                .setScale(0, RoundingMode.HALF_UP).intValue()
                        )
                        .sum();

                LOG.log(Level.INFO, "Computed Total of ExpenseItems = {0}",
                        total);

                if ((total > e.getAmount() + n) || (total < e.getAmount() - n)) {
                    String errmsg = entity.getClass().getSimpleName()
                            + " details are not accurate: computed total =  "
                            + total + " / provided amount = " + e.getAmount();
                    LOG.log(Level.WARNING, errmsg);
                    throw new WebApplicationException(errmsg);
                }
            }

        });
    }

    public Response put(Consumer<T> updateActions, SecurityContext sec, T entity,
            String professional) {

        ExpensePK pk = new ExpensePK(entity.getId(), this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Updating {0} {1}",
                new Object[]{entity.getClass().getSimpleName(),
                    getFacade().prettyPrintPK(pk)});
        return super.put(entity, pk, e -> {
            if (!e.isCancelled()) {

                updateActions.accept(e);

                e.setCategories(entity.getCategories());

                e.setCancelled(entity.isCancelled());
                if (e.isCancelled()) {
                    e.setCancellationDate(new Date());
                }

            }// ignore if expense is cancelled
        });
    }
}
