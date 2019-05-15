/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.returneditem.boundaries;

import fr.trendev.comptandye.productrecord.boundaries.AbstractProductRecordService;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.bill.entities.BillPK;
import fr.trendev.comptandye.returneditem.entities.ReturnedItem;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.returneditem.controllers.ReturnedItemFacade;
import fr.trendev.comptandye.bill.controllers.ProvideBillFacadeVisitor;
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
@Path("ReturnedItem")
@RolesAllowed({"Administrator"})
public class ReturnedItemService extends AbstractProductRecordService<ReturnedItem> {

    @Inject
    ReturnedItemFacade returnedItemFacade;

    @Inject
    ProvideBillFacadeVisitor visitor;

    private final Logger LOG = Logger.getLogger(ReturnedItemService.class.
            getName());

    public ReturnedItemService() {
        super(ReturnedItem.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<ReturnedItem, Long> getFacade() {
        return returnedItemFacade;
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
        LOG.log(Level.INFO, "REST request to get ReturnedItem : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response post(@Context SecurityContext sec,
            ReturnedItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Creating ReturnedItem {0}", super.
                stringify(entity));

        String proEmail = this.getProEmail(sec, professional);

        return super.post(entity, proEmail, e -> {

            //finds the Bill
            if (e.getCancelledBill() == null) {

                String errmsg = "A cancelled Bill must be provided";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            AbstractFacade<? extends Bill, BillPK> facade = e.getCancelledBill().
                    accept(
                            visitor);

            BillPK pk = new BillPK(
                    e.getCancelledBill().getReference(),
                    e.getCancelledBill().getDeliveryDate(),
                    proEmail);

            Bill bill = facade.find(pk);

            if (bill == null) {
                String errmsg = "Bill " + facade.prettyPrintPK(pk)
                        + " not found for user " + proEmail + " !";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (!bill.isCancelled()) {
                String errmsg = "Bill " + facade.prettyPrintPK(pk)
                        + " has not been cancelled and is not available for ReturnedItem creation";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            //adds the bill in the current ReturnedItem
            e.setCancelledBill(bill);

        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response put(@Context SecurityContext sec,
            ReturnedItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Updating ReturnedItem {0}", entity.getId());
        return super.put(entity, this.getProEmail(sec, professional));
    }

    /**
     * Delete a ReturnedItem
     *
     * @param id the ReturnedItem's id
     * @return OK if success, HTTP Unexpected Response otherwiseS
     */
    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting ReturnedItem {0}", id);
        return super.delete(id, e -> {
        });
    }

}
