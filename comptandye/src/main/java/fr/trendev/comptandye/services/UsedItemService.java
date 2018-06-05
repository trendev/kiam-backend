/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.UsedItem;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.UsedItemFacade;
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
@Path("UsedItem")
@RolesAllowed({"Administrator"})
public class UsedItemService extends AbstractProductRecordService<UsedItem> {

    @Inject
    UsedItemFacade usedItemFacade;

    private final Logger LOG = Logger.getLogger(UsedItemService.class.
            getName());

    public UsedItemService() {
        super(UsedItem.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<UsedItem, Long> getFacade() {
        return usedItemFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the UsedItem list");
        return super.findAll();
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
        LOG.log(Level.INFO, "REST request to get UsedItem : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response post(@Context SecurityContext sec,
            UsedItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Creating UsedItem {0}", super.
                stringify(entity));

        String proEmail = this.getProEmail(sec, professional);

        return super.post(entity, proEmail, e -> {
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response put(@Context SecurityContext sec,
            UsedItem entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Updating UsedItem {0}", entity.getId());
        return super.put(entity, this.getProEmail(sec, professional));
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting UsedItem {0}", id);
        return super.delete(id, e -> {
        });
    }

}
