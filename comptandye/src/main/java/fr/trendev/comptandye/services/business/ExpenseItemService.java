/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.business;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.entities.ExpenseItem;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.sessions.ExpenseItemFacade;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("ExpenseItem")
@RolesAllowed({"Administrator"})
public class ExpenseItemService extends AbstractCommonService<ExpenseItem, Long> {

    @Inject
    ExpenseItemFacade expenseItemFacade;

    private final Logger LOG = Logger.getLogger(ExpenseItemService.class.
            getName());

    public ExpenseItemService() {
        super(ExpenseItem.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<ExpenseItem, Long> getFacade() {
        return expenseItemFacade;
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
        LOG.log(Level.INFO, "REST request to get ExpenseItem : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(ExpenseItem entity) {
        LOG.log(Level.INFO, "Creating ExpenseItem {0}", super.stringify(entity));
        return super.post(entity, e -> {
            e.setId(null);
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(ExpenseItem entity) {
        LOG.log(Level.INFO, "Updating ExpenseItem {0}", entity.getId());
        return super.put(entity, entity.getId(),
                e -> {
            e.setDescription(entity.getDescription());
            e.setAmount(entity.getAmount());
            e.setQty(entity.getQty());
            e.setVatRate(entity.getVatRate());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting ExpenseItem {0}", id);
        return super.delete(id, e -> {
        });
    }

}
