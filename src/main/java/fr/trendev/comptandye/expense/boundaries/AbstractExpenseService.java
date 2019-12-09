/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.expense.boundaries;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.expense.entities.Expense;
import fr.trendev.comptandye.expense.entities.ExpensePK;
import fr.trendev.comptandye.expenseitem.entities.ExpenseItem;
import fr.trendev.comptandye.payment.entities.Payment;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
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
 * @param <T> subtype of Expense
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

                    // override the id (security reason)
                    e.setId(UUIDGenerator.generateID());

                    // fresh new expense, default is not cancelled
                    e.setCancelled(false);
                    e.setCancellationDate(null);

                    e.setIssueDate(new Date());

                    // check if businesses are provided
                    if (e.getBusinesses() == null || e.getBusinesses().isEmpty()) {
                        String errmsg = "Businesses in " + entity.getClass().
                                getSimpleName() + " must be provided";
                        LOG.log(Level.WARNING, errmsg);
                        throw new WebApplicationException(errmsg);
                    }

                    // control the expense's amount
                    if (e.getAmount() <= 0) {
                        String errmsg = "Amount in " + entity.getClass().getSimpleName()
                        + " must be greater than 0";
                        LOG.log(Level.WARNING, errmsg);
                        throw new WebApplicationException(errmsg);
                    }

                    // check if payments are provided
                    if (e.getPayments() == null || e.getPayments().isEmpty()) {
                        String errmsg = "Payments in " + entity.getClass().
                                getSimpleName() + " must be provided";
                        LOG.log(Level.WARNING, errmsg);
                        throw new WebApplicationException(errmsg);
                    }

                    // control the expense has been paid
                    if (e.getPaymentDate() == null) {
                        String errmsg = "Payment date in " + entity.getClass().
                                getSimpleName() + " must be provided";
                        LOG.log(Level.WARNING, errmsg);
                        throw new WebApplicationException(errmsg);
                    } else {
                        // TODO : control that the payment date is not in future compared from now
                    }

                    // sum of the payments
                    int total = e.getPayments().stream()
                            .mapToInt(Payment::getAmount)
                            .sum();

                    if (total != e.getAmount()) {
                        String errmsg = "Amount is " + e.getAmount()
                        + " but the total amount computed is " + total;
                        LOG.log(Level.WARNING, errmsg);
                        throw new WebApplicationException(errmsg);
                    } else { // override the payments ids (security reason)
                        e.getPayments().forEach((p) -> {
                            p.setId(UUIDGenerator.generateID());
                        });
                    }

                    // if the expense is vat inclusive, the user should provide expense details
                    // if there are not expense items, the vat inclusive value is reset to false
                    if (e.isVatInclusive()
                    && (e.getExpenseItems() == null || e.getExpenseItems().isEmpty())) {
                        LOG.log(Level.INFO,
                                "vatInclusive field is reset to false in {0}: no ExpenseItem found",
                                entity.
                                        getClass().getSimpleName());
                        e.setVatInclusive(false);
                    }

                    /**
                     * Check accuracy of the provided amount and the total
                     * computed amount from the expense items (the expense
                     * details). This algorithm is important helping the system
                     * to control non accurate calculation from other systems.
                     * The total computed amount should be between a lower and a
                     * higher bound : e.getAmount() - n <= total_computed <=
                     * e.getAmount() + n.
                     */
                    if (e.getExpenseItems() != null
                    && !e.getExpenseItems().isEmpty()) {
                        e.setVatInclusive(true); //override vat inclusive value anyway
                        // computes how many items are found in this Expense 
                        int n = e.getExpenseItems().stream()
                                .mapToInt(ExpenseItem::getQty)
                                .sum();
                        LOG.log(Level.INFO, "Checking number of expense items... n = {0}",
                                n);

                        total = e.getExpenseItems().stream()
                                .mapToInt(ei
                                        -> ei.getQty() * new BigDecimal(ei.getAmount()).
                                multiply(ei.getVatRate()
                                        .add(new BigDecimal(100)))
                                .divide(new BigDecimal(100))
                                .setScale(0, RoundingMode.HALF_UP).intValue()
                                )
                                .sum();

                        LOG.log(Level.INFO, "Computed Total of expense items = {0}",
                                total);

                        if ((total > e.getAmount() + n) || (total < e.getAmount() - n)) {
                            String errmsg = entity.getClass().getSimpleName()
                            + " details are not accurate: computed total =  "
                            + total + " / provided amount = " + e.getAmount();
                            LOG.log(Level.WARNING, errmsg);
                            throw new WebApplicationException(errmsg);
                        }

                        // override the expense items ids (security reason)
                        e.getExpenseItems().forEach((ei) -> {
                            ei.setId(UUIDGenerator.generateID());
                        });
                    }

                }
        );
    }

    /**
     * Update an Expense. Updated fields depend on the Expense Type.
     * expenseItems cannot be updated using this method
     *
     * @param updateActions the update actions
     * @param sec the security context
     * @param entity the payload
     * @param professional the owner of the Expense
     * @return HTTP 200 OK if successful, an error message otherwise
     */
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
