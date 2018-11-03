/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.offering.entities.OfferingPK;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.service.entities.Service;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
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
@Path("Service")
@RolesAllowed({"Administrator", "Professional"})
public class ServiceService extends AbstractOfferingService<Service> {

    private final Logger LOG = Logger.getLogger(ServiceService.class.
            getName());

    public ServiceService() {
        super(Service.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Service, OfferingPK> getFacade() {
        return serviceFacade;
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
        OfferingPK pk = new OfferingPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Service : {0}", serviceFacade.
                prettyPrintPK(
                        pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Service entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        if (entity.getBusinesses() == null || entity.getBusinesses().isEmpty()) {
            throw new WebApplicationException("No Business provided !");
        }

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade, Service::setProfessional,
                Professional::getOfferings, e -> {
            e.setId(null);
        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Service entity,
            @QueryParam("professional") String professional) {

        if (entity.getBusinesses() == null || entity.getBusinesses().isEmpty()) {
            throw new WebApplicationException("No Business provided !");
        }

        OfferingPK pk = new OfferingPK(entity.getId(), this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Updating Service {0}", serviceFacade.
                prettyPrintPK(pk));
        return super.put(entity, pk, e -> {
            e.setName(entity.getName());
            e.setShortname(entity.getShortname());
            e.setPrice(entity.getPrice());
            e.setDuration(entity.getDuration());
            e.setBusinesses(entity.getBusinesses());
        });
    }

    /**
     * Prepares and deletes a Service.
     *
     * If a Service has been purchased, it has been associated to a Bill and
     * cannot be deleted! Delete the Bill first (this operation is only allowed
     * for Administrator)
     *
     * @param sec the security context
     * @param id the Entity's id
     * @param professional the owner's email
     * @return HTTP OK if no error occurs
     */
    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        OfferingPK pk = new OfferingPK(id, this.getProEmail(sec, professional));

        LOG.log(Level.INFO, "Deleting Service {0}", serviceFacade.
                prettyPrintPK(pk));

        return super.delete(pk, e -> {
        });
    }

    @Path("{id}/purchasedOfferings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getPurchasedOfferings(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        OfferingPK pk = new OfferingPK(id, this.getProEmail(sec,
                professional));
        super.getPurchasedOfferings(ar, pk);
    }

    @Path("{id}/parentPacks")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getParentPacks(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        OfferingPK pk = new OfferingPK(id, this.getProEmail(sec,
                professional));
        super.getParentPacks(ar, pk);
    }
}
