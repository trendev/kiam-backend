/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.utils.AssociationManagementEnum;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.json.Json;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Pack")
@RolesAllowed({"Administrator", "Professional"})
public class PackService extends AbstractOfferingService<Pack> {

    private final Logger LOG = Logger.getLogger(PackService.class.
            getName());

    public PackService() {
        super(Pack.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Pack, OfferingPK> getFacade() {
        return packFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Pack list");
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
        OfferingPK pk = new OfferingPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Pack : {0}", packFacade.
                prettyPrintPK(pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Pack entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        if (entity.getBusinesses() == null || entity.getBusinesses().isEmpty()) {
            throw new WebApplicationException("No Business provided !");
        }

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade, Pack::setProfessional,
                Professional::getOfferings, e -> {
            e.setId(null);

            if (!e.getOfferings().isEmpty()) {
                //checks provided offerings are owned by the professional
                List<Offering> offerings = e.getOfferings().stream()
                        .map(o -> {
                            Offering _o = checkOfferingIntegrity(o, email);
                            _o.getParentPacks().add(e);
                            return _o;
                        })
                        .collect(Collectors.toList());

                e.setOfferings(offerings);
            }
        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Pack entity,
            @QueryParam("professional") String professional) {

        if (entity.getBusinesses() == null || entity.getBusinesses().isEmpty()) {
            throw new WebApplicationException("No Business provided !");
        }

        String email = this.getProEmail(sec, professional);

        OfferingPK pk = new OfferingPK(entity.getId(), email);

        LOG.log(Level.INFO, "Updating Pack {0}", packFacade.
                prettyPrintPK(pk));
        return super.put(entity, pk, e -> {
            e.setName(entity.getName());
            e.setPrice(entity.getPrice());
            e.setDuration(entity.getDuration());
            e.setBusinesses(entity.getBusinesses());

            //remove previous parentPacks dependencies and reset the offerings
            if (!e.getOfferings().isEmpty()) {
                e.getOfferings().forEach(o -> o.getParentPacks().remove(e));
                e.setOfferings(new LinkedList<>());
            }

            if (!entity.getOfferings().isEmpty()) {
                //checks provided offerings are owned by the professional
                List<Offering> offerings = entity.getOfferings().stream()
                        .map(o -> {
                            Offering _o = checkOfferingIntegrity(o, email);
                            _o.getParentPacks().add(e);
                            return _o;
                        })
                        .collect(Collectors.toList());

                e.setOfferings(offerings);
            }//let the offerings empty

        });
    }

    /**
     * Prepares and deletes a Pack.
     *
     * If a Pack has been purchased, it has been associated to a Bill and cannot
     * be deleted! Delete the Bill first (this operation is only allowed for
     * Administrator)
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

        OfferingPK pk = new OfferingPK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Pack {0}", packFacade.
                prettyPrintPK(pk));

        return super.delete(pk, e -> {
            e.getOfferings().forEach(o -> o.getParentPacks().remove(e));
        });
    }

    @Path("{packid}/addService/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addService(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        OfferingPK packPK = new OfferingPK(packid, this.getProEmail(sec,
                professional));

        OfferingPK offeringPK = new OfferingPK(offeringid, this.getProEmail(sec,
                professional));

        return super.<Service, OfferingPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                packPK,
                serviceFacade,
                offeringPK, Service.class,
                (p, o) ->
                p.getOfferings().add(o) && o.getParentPacks().add(p));
    }

    @Path("{packid}/removeService/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeService(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        OfferingPK packPK = new OfferingPK(packid, this.getProEmail(sec,
                professional));

        OfferingPK offeringPK = new OfferingPK(offeringid, this.getProEmail(sec,
                professional));

        return super.<Service, OfferingPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                packPK,
                serviceFacade,
                offeringPK, Service.class,
                (p, o) ->
                p.getOfferings().remove(o) && o.getParentPacks().remove(p));
    }

    @Path("{packid}/addPack/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPack(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        //avoid to add a pack in itself (infinite loop)
        if (packid == offeringid) {
            LOG.log(Level.WARNING, "Pack {0} cannot be added in itself", packid);
            return Response.status(Response.Status.EXPECTATION_FAILED)
                    .entity(Json.createObjectBuilder()
                            .add("error", "Pack " + packid
                                    + " cannot be added in itself")
                            .build()
                    )
                    .build();
        }

        OfferingPK packPK = new OfferingPK(packid, this.getProEmail(sec,
                professional));

        OfferingPK offeringPK = new OfferingPK(offeringid, this.getProEmail(sec,
                professional));

        return super.<Pack, OfferingPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                packPK,
                packFacade,
                offeringPK, Pack.class,
                (p, o) ->
                p.getOfferings().add(o) && o.getParentPacks().add(p));
    }

    @Path("{packid}/removePack/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePack(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        OfferingPK packPK = new OfferingPK(packid, this.getProEmail(sec,
                professional));

        OfferingPK offeringPK = new OfferingPK(offeringid, this.getProEmail(sec,
                professional));

        return super.<Pack, OfferingPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                packPK,
                packFacade,
                offeringPK, Pack.class,
                (p, o) ->
                p.getOfferings().remove(o) && o.getParentPacks().remove(p));
    }

    @Path("{id}/purchasedOfferings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPurchasedOfferings(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        OfferingPK pk = new OfferingPK(id, this.getProEmail(sec,
                professional));
        return super.getPurchasedOfferings(pk);
    }

    @Path("{id}/parentPacks")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getParentPacks(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        OfferingPK pk = new OfferingPK(id, this.getProEmail(sec,
                professional));
        return super.getParentPacks(pk);
    }
}
