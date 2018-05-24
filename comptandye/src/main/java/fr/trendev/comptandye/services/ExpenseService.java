/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.sessions.AbstractFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
@RolesAllowed({"Administrator", "Professional"})
public class ExpenseService extends AbstractExpenseService<Expense> {

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

        return super.post(sec, entity, professional);

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

        return super.put(e -> {
            e.setDescription(entity.getDescription());
            e.setPaymentDate(entity.getPaymentDate());
            e.setProvider(entity.getProvider());
            e.setBusinesses(entity.getBusinesses());
        },
                sec, entity, professional);
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
