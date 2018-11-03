/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.ClassicExpense;
import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.sessions.ClassicExpenseFacade;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("ClassicExpense")
@RolesAllowed({"Administrator", "Professional"})
public class ClassicExpenseService extends AbstractExpenseService<ClassicExpense> {

    @Inject
    private ClassicExpenseFacade classicFacade;

    private final Logger LOG = Logger.getLogger(ClassicExpenseService.class.
            getName());

    public ClassicExpenseService() {
        super(ClassicExpense.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<ClassicExpense, ExpensePK> getFacade() {
        return classicFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
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
        LOG.log(Level.INFO, "REST request to get ClassicExpense : {0}",
                classicFacade.prettyPrintPK(pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response post(@Context SecurityContext sec, ClassicExpense entity,
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
    public Response put(@Context SecurityContext sec, ClassicExpense entity,
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

        LOG.log(Level.INFO, "Deleting Expense {0}", classicFacade.
                prettyPrintPK(pk));
        return super.delete(pk, e -> e.getProfessional().getExpenses().remove(
                e));
    }
}
