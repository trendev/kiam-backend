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
import fr.trendev.comptandye.sessions.ExpenseFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Expense")
@RolesAllowed({"Administrator", "Professional"})
public class ExpenseService extends AbstractCommonService<Expense, ExpensePK> {

    @Inject
    ExpenseFacade expenseFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    private final Logger LOG = Logger.getLogger(ExpenseService.class.
            getName());

    public ExpenseService() {
        super(Expense.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Expense, ExpensePK> getFacade() {
        return expenseFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Expense list");
        return super.findAll();
    }

    @RolesAllowed({"Administrator"})
    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        ExpensePK pk = new ExpensePK(id, professional);
        LOG.log(Level.INFO, "REST request to get Expense : {0}",
                expenseFacade.prettyPrintPK(pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Expense entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade, Expense::setProfessional,
                Professional::getExpenses, e -> {
            e.setId(null);
            e.setCancelled(false);
            e.setCancellationDate(null);
            e.setIssueDate(new Date());

            if (e.getBusinesses() == null || e.getBusinesses().isEmpty()) {
                String errmsg = "Businesses in Expense must be provided";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (e.getAmount() <= 0) {
                String errmsg = "Amount in Expense must be greater than 0";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (e.getPayments() == null || e.getPayments().isEmpty()) {
                String errmsg = "Payments in Expense must be provided";
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
                        "vatInclusive field is reset to false in Expense: no ExpenseItem found");
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
                    String errmsg = "Expense details are not accurate: computed total =  "
                            + total + " / provided amount = " + e.getAmount();
                    LOG.log(Level.WARNING, errmsg);
                    throw new WebApplicationException(errmsg);
                }
            }

        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Amount, Currency, Payments and ExpenseItems/vatInclusive cannot be
     * updated using this method.
     */
    public Response put(@Context SecurityContext sec, Expense entity,
            @QueryParam("professional") String professional) {

        ExpensePK pk = new ExpensePK(entity.getId(), this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Updating Expense {0}", expenseFacade.
                prettyPrintPK(pk));
        return super.put(entity, pk, e -> {
            if (!e.isCancelled()) {
                e.setDescription(entity.getDescription());
                e.setPaymentDate(entity.getPaymentDate());
                e.setProvider(entity.getProvider());
                e.setCategories(entity.getCategories());
                e.setBusinesses(entity.getBusinesses());

                e.setCancelled(entity.isCancelled());
                if (e.isCancelled()) {
                    e.setCancellationDate(new Date());
                }

            }// ignore if expense is cancelled
        });
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        ExpensePK pk = new ExpensePK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Expense {0}", expenseFacade.
                prettyPrintPK(pk));
        return super.delete(pk, e -> e.getProfessional().getExpenses().remove(
                e));
    }
}
