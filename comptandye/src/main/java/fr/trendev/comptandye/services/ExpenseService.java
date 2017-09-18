/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ExpenseFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
public class ExpenseService extends AbstractCommonService<Expense, ExpensePK> {

    @Inject
    ExpenseFacade expenseFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    private static final Logger LOG = Logger.getLogger(ExpenseService.class.
            getName());

    public ExpenseService() {
        super(Expense.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Expense list");
        return super.findAll(expenseFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(expenseFacade);
    }

    @Path("key")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@QueryParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        ExpensePK pk = new ExpensePK(id, professional);
        LOG.log(Level.INFO, "REST request to get Expense : {0}", expenseFacade.
                prettyPrintPK(pk));
        return super.find(expenseFacade, pk, refresh);
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
                expenseFacade, professionalFacade, Expense::setProfessional,
                Professional::getExpenses, e -> {
            int total = e.getPayments().stream()
                    .mapToInt(Payment::getAmount)
                    .sum();
            if (total != e.getAmount()) {
                LOG.log(Level.WARNING,
                        "Total amount is {0} but the total amount computed is {1}. Amount value is now {1}",
                        new Object[]{e.getAmount(), total});
            }
        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Amount and Payments can be updated using this method.
     */
    public Response put(@Context SecurityContext sec, Expense entity,
            @QueryParam("professional") String professional) {

        ExpensePK pk = new ExpensePK(entity.getId(), this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Updating Expense {0}", expenseFacade.
                prettyPrintPK(pk));
        return super.put(entity, expenseFacade, pk, e -> {

            e.setName(entity.getName());
            e.setPaymentDate(entity.getPaymentDate());
            e.setInvoiceRef(entity.getInvoiceRef());
            e.setCategories(entity.getCategories());
            e.setBusinesses(entity.getBusinesses());
        });
    }

    @Path("key")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @QueryParam("id") Long id,
            @QueryParam("professional") String professional) {

        ExpensePK pk = new ExpensePK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Expense {0}", expenseFacade.
                prettyPrintPK(pk));
        return super.delete(expenseFacade, pk,
                e -> e.getProfessional().getExpenses().remove(e));
    }
}