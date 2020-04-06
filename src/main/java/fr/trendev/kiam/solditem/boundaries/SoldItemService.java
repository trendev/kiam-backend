/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.solditem.boundaries;

import fr.trendev.kiam.productrecord.boundaries.AbstractProductRecordService;
import fr.trendev.kiam.bill.entities.Bill;
import fr.trendev.kiam.bill.entities.BillPK;
import fr.trendev.kiam.solditem.entities.SoldItem;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.solditem.controllers.SoldItemFacade;
import fr.trendev.kiam.bill.controllers.ProvideBillFacadeVisitor;
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
@Path("SoldItem")
@RolesAllowed({"Administrator"})
public class SoldItemService extends AbstractProductRecordService<SoldItem> {

    @Inject
    SoldItemFacade soldItemFacade;

    @Inject
    ProvideBillFacadeVisitor visitor;

    private final Logger LOG = Logger.getLogger(SoldItemService.class.
            getName());

    public SoldItemService() {
        super(SoldItem.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected SoldItemFacade getFacade() {
        return soldItemFacade;
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
    public Response find(@PathParam("id") String id,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get SoldItem : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response post(@Context SecurityContext sec,
            SoldItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Creating SoldItem {0}", super.
                stringify(entity));

        String proEmail = this.getProEmail(sec, professional);

        return super.post(entity, proEmail, e -> {

            //finds the Bill
            if (e.getBill() == null) {

                String errmsg = "A Bill must be provided";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            AbstractFacade<? extends Bill, BillPK> facade = e.getBill().accept(
                    visitor);

            BillPK pk = new BillPK(
                    e.getBill().getReference(),
                    e.getBill().getDeliveryDate(),
                    proEmail);

            Bill bill = facade.find(pk);

            if (bill == null) {
                String errmsg = "Bill " + facade.prettyPrintPK(pk)
                        + " not found for user " + proEmail + " !";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            if (bill.isCancelled()) {
                String errmsg = "Bill " + facade.prettyPrintPK(pk)
                        + " has been cancelled and is not available for SoldItem creation";
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            //adds the bill in the current SoldItem
            e.setBill(bill);

        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response put(@Context SecurityContext sec,
            SoldItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Updating SoldItem {0}", entity.getId());
        return super.put(entity, this.getProEmail(sec, professional));
    }

    /**
     * Delete a SoldItem
     *
     * @param id the SoldItem's id
     * @return OK if success, HTTP Unexpected Response otherwiseS
     */
    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") String id) {
        LOG.log(Level.INFO, "Deleting SoldItem {0}", id);
        return super.delete(id, e -> {
        });
    }

}
