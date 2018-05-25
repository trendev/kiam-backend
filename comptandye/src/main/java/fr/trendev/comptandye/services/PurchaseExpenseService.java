/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.entities.PurchaseExpense;
import fr.trendev.comptandye.entities.PurchasedItem;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.PurchaseExpenseFacade;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("PurchaseExpense")
@RolesAllowed({"Administrator", "Professional"})
public class PurchaseExpenseService extends AbstractExpenseService<PurchaseExpense> {

    @Inject
    private PurchaseExpenseFacade purchaseFacade;

    private final Logger LOG = Logger.getLogger(PurchaseExpenseService.class.
            getName());

    public PurchaseExpenseService() {
        super(PurchaseExpense.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<PurchaseExpense, ExpensePK> getFacade() {
        return purchaseFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the PurchaseExpense list");
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
        LOG.log(Level.INFO, "REST request to get PurchaseExpense : {0}",
                purchaseFacade.prettyPrintPK(pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response post(@Context SecurityContext sec, PurchaseExpense entity,
            @QueryParam("professional") String professional) {
        return super.post(sec, entity, professional);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * No modification allowed
     */
    // TODO : cancel purchasedItems if the PurchaseExpense is cancelled
    public Response put(@Context SecurityContext sec, PurchaseExpense entity,
            @QueryParam("professional") String professional) {

        return super.put(e -> {
        }, sec, entity, professional);
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        ExpensePK pk = new ExpensePK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting PurchaseExpense {0}", purchaseFacade.
                prettyPrintPK(pk));
        return super.delete(pk, e -> {
            e.getProfessional().getExpenses().remove(e);
            e.getPurchasedItems().forEach(pi -> pi.setPurchaseExpense(null));
            e.setPurchasedItems(null);
        });
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}/purchasedItems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPurchasedItems(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        ExpensePK pk = new ExpensePK(id, this.getProEmail(sec,
                professional));

        return this.provideRelation(pk,
                PurchaseExpense::getPurchasedItems, PurchasedItem.class);
    }

}
