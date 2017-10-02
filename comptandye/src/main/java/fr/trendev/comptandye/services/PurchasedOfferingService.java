/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.PurchasedOfferingFacade;
import fr.trendev.comptandye.visitors.ProvideOfferingFacadeVisitor;
import java.util.Optional;
import java.util.function.Function;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("PurchasedOffering")
public class PurchasedOfferingService extends AbstractCommonService<PurchasedOffering, Long> {

    @Inject
    PurchasedOfferingFacade purchasedOfferingFacade;

    @Inject
    ProvideOfferingFacadeVisitor visitor;

    private static final Logger LOG = Logger.getLogger(
            PurchasedOfferingService.class.
                    getName());

    public PurchasedOfferingService() {
        super(PurchasedOffering.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<PurchasedOffering, Long> getFacade() {
        return purchasedOfferingFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the PurchasedOffering list");
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
        LOG.log(Level.INFO, "REST request to get PurchasedOffering : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(PurchasedOffering entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Creating PurchasedOffering {0}", super.stringify(
                entity));

        if (professional == null) {
            throw new WebApplicationException(
                    "Profession email is not provided !");
        }

        return super.post(entity, e -> {
            e.setId(null);

            if (e.getOffering() == null) {
                throw new WebApplicationException("No Offering provided !");
            }

            e.setOffering(Optional.ofNullable(e.getOffering().accept(visitor).
                    find(new OfferingPK(e.getOffering().getId(), professional)))
                    .map(Function.identity())
                    .orElseThrow(() -> new WebApplicationException()));
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(PurchasedOffering entity,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Updating PurchasedOffering {0}", entity.getId());

        if (professional == null) {
            throw new WebApplicationException(
                    "Profession email is not provided !");
        }

        return super.put(entity, entity.getId(),
                e -> {

            if (e.getOffering() == null) {
                throw new WebApplicationException("No Offering provided !");
            }

            e.setQty(entity.getQty());
            e.setOffering(Optional.ofNullable(e.getOffering().accept(visitor).
                    find(new OfferingPK(e.getOffering().getId(), professional)))
                    .map(Function.identity())
                    .orElseThrow(() -> new WebApplicationException()));
        });
    }

    /**
     * Deletes a free PurchasedOffering (not yet associated with a bill). If a
     * PurchasedOffering is associated with a Bill, delete the Bill first (this
     * operation is only allowed for Administrator)
     *
     * @param id the Entity's id
     * @return HTTP OK if no error occurs
     */
    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting PurchasedOffering {0}", id);
        return super.delete(id, e -> {
        });
    }
}
