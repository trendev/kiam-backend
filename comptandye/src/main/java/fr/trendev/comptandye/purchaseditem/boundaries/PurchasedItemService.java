/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.purchaseditem.boundaries;

import fr.trendev.comptandye.productrecord.boundaries.AbstractProductRecordService;
import fr.trendev.comptandye.expense.entities.ExpensePK;
import fr.trendev.comptandye.purchaseexpense.entities.PurchaseExpense;
import fr.trendev.comptandye.purchaseditem.entities.PurchasedItem;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.purchaseexpense.controllers.PurchaseExpenseFacade;
import fr.trendev.comptandye.purchaseditem.controllers.PurchasedItemFacade;
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
@Path("PurchasedItem")
@RolesAllowed({"Administrator"})
public class PurchasedItemService extends AbstractProductRecordService<PurchasedItem> {

    @Inject
    PurchasedItemFacade purchasedItemFacade;

    @Inject
    PurchaseExpenseFacade purchaseExpenseFacade;

    private final Logger LOG = Logger.getLogger(PurchasedItemService.class.
            getName());

    public PurchasedItemService() {
        super(PurchasedItem.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<PurchasedItem, Long> getFacade() {
        return purchasedItemFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
    }

    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("id") Long id,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get PurchasedItem : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response post(@Context SecurityContext sec,
            PurchasedItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Creating PurchasedItem {0}", super.
                stringify(entity));

        String proEmail = this.getProEmail(sec, professional);

        return super.post(entity, proEmail, e -> {
            //finds the PurchaseExpense

            if (e.getPurchaseExpense() == null) {

                String errmsg = "A PurchaseExpense must be provided";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            PurchaseExpense pe = purchaseExpenseFacade.find(new ExpensePK(e.
                    getPurchaseExpense().getId(), proEmail));

            if (pe == null) {
                String errmsg = "PurchaseExpense " + e.
                        getPurchaseExpense().getId()
                        + " not found for user " + proEmail + " !";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (pe.isCancelled()) {
                String errmsg = "PurchaseExpense " + e.
                        getPurchaseExpense().getId()
                        + " has been cancelled and is not available for PurchasedItem creation";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            //adds the current PurchasedItem inside
            e.setPurchaseExpense(pe);
            pe.getPurchasedItems().add(e);

        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response put(@Context SecurityContext sec,
            PurchasedItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Updating PurchasedItem {0}", entity.getId());
        return super.put(entity, this.getProEmail(sec, professional));
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting PurchasedItem {0}", id);
        return super.delete(id, e -> {
            e.getPurchaseExpense().getPurchasedItems().remove(e);
            e.setPurchaseExpense(null);
        });
    }

}
