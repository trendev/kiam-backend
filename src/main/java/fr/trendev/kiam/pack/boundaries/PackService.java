/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.pack.boundaries;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.kiam.common.boundaries.AssociationManagementEnum;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.offering.boundaries.AbstractOfferingService;
import fr.trendev.kiam.offering.entities.Offering;
import fr.trendev.kiam.offering.entities.OfferingPK;
import fr.trendev.kiam.pack.entities.Pack;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.sale.entities.Sale;
import fr.trendev.kiam.service.entities.Service;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
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
@Path("Pack")
@RolesAllowed({"Administrator", "Professional"})
public class PackService extends AbstractOfferingService<Pack> {

    @Inject
    ObjectMapper om;

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
    public Response find(@PathParam("id") String id,
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
                    e.setId(UUIDGenerator.generateID());

                    // control if the provided offerings are owned by the same professional or not (integrity)
                    // the offerings must exist and cannot be persisted creating the pack
                    if (!e.getOfferings().isEmpty()) {
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
            e.setShortname(entity.getShortname());
            e.setPrice(entity.getPrice());
            e.setDuration(entity.getDuration());
            e.setBusinesses(entity.getBusinesses());

            //remove previous parentPacks dependencies and reset the offerings
            if (!e.getOfferings().isEmpty()) {
                e.getOfferings().forEach(o -> o.getParentPacks().remove(e));
                e.setOfferings(new LinkedList<>());
            }

            // control if the provided offerings are owned by the same professional or not (integrity)
            // the offerings must exist and cannot be persisted creating the pack
            if (!entity.getOfferings().isEmpty()) {
                List<Offering> offerings = entity.getOfferings().stream()
                        .map(o -> {
                            Offering _o = checkOfferingIntegrity(o, email);
                            this.controlPackCyclicReferences(e, _o);
                            _o.getParentPacks().add(e);
                            return _o;
                        })
                        .collect(Collectors.toList());

                e.setOfferings(offerings);
            }// no matter if the entity's offering is empty, the pack's offering won't be updated (erased)

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
            @PathParam("id") String id,
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
            @PathParam("packid") String packid,
            @PathParam("offeringid") String offeringid,
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
                (p, o)
                -> p.getOfferings().add(o) && o.getParentPacks().add(p));
    }

    @Path("{packid}/removeService/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeService(@Context SecurityContext sec,
            @PathParam("packid") String packid,
            @PathParam("offeringid") String offeringid,
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
                (p, o)
                -> p.getOfferings().remove(o) && o.getParentPacks().remove(p));
    }

    @Path("{packid}/addPack/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPack(@Context SecurityContext sec,
            @PathParam("packid") String packid,
            @PathParam("offeringid") String offeringid,
            @QueryParam("professional") String professional) {

        OfferingPK packPK = new OfferingPK(packid, this.getProEmail(sec,
                professional));

        OfferingPK offeringPK = new OfferingPK(offeringid, this.getProEmail(sec,
                professional));

        return super.<Pack, OfferingPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                packPK,
                packFacade,
                offeringPK, Pack.class,
                (p, o) -> {
                    this.controlPackCyclicReferences(p, o);
                    return p.getOfferings().add(o) && o.getParentPacks().add(p);
                }
        );
    }

    private void controlPackCyclicReferences(Pack pack, Offering offering) {

        if (pack.getId() == null) {
            String msg = "Controlling cyclic dependencies of Pack ["
                    + pack.getName()
                    + "] : pack id is missing";
            LOG.log(Level.WARNING, msg);
            throw new IllegalStateException(msg);
        }

        if (offering.getId() == null) {
            String msg = "Controlling cyclic dependencies of Pack ["
                    + pack.getName() + "] : offering id is missing in Offering ["
                    + offering.getName() + "]";
            LOG.log(Level.WARNING, msg);
            throw new IllegalStateException(msg);
        }

        //avoid to add a pack in itself (infinite loop)
        if (pack.getId().equals(offering.getId())) {
            throw new BadRequestException("Pack " + pack.getId()
                    + " cannot be addded in itself");
        }

        //prevent to include a parent pack (cyclic references)
        if (pack.getAboveParentPacks().contains(offering)) {
            throw new BadRequestException("Pack " + offering.getId()
                    + " cannot be added to Pack " + pack.getId()
                    + " because Pack " + offering.getId()
                    + " already contains Pack " + pack.getId() + " !!!");
        }
    }

    @Path("{packid}/removePack/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePack(@Context SecurityContext sec,
            @PathParam("packid") String packid,
            @PathParam("offeringid") String offeringid,
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
                (p, o)
                -> p.getOfferings().remove(o) && o.getParentPacks().remove(p));
    }

    @Path("{packid}/addSale/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSale(@Context SecurityContext sec,
            @PathParam("packid") String packid,
            @PathParam("offeringid") String offeringid,
            @QueryParam("professional") String professional) {

        OfferingPK packPK = new OfferingPK(packid, this.getProEmail(sec,
                professional));

        OfferingPK offeringPK = new OfferingPK(offeringid, this.getProEmail(sec,
                professional));

        return super.<Sale, OfferingPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                packPK,
                saleFacade,
                offeringPK, Sale.class,
                (p, o)
                -> p.getOfferings().add(o) && o.getParentPacks().add(p));
    }

    @Path("{packid}/removeSale/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSale(@Context SecurityContext sec,
            @PathParam("packid") String packid,
            @PathParam("offeringid") String offeringid,
            @QueryParam("professional") String professional) {

        OfferingPK packPK = new OfferingPK(packid, this.getProEmail(sec,
                professional));

        OfferingPK offeringPK = new OfferingPK(offeringid, this.getProEmail(sec,
                professional));

        return super.<Sale, OfferingPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                packPK,
                saleFacade,
                offeringPK, Sale.class,
                (p, o)
                -> p.getOfferings().remove(o) && o.getParentPacks().remove(p));
    }

    @Path("{id}/purchasedOfferings")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getPurchasedOfferings(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("id") String id,
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
            @PathParam("id") String id,
            @QueryParam("professional") String professional) {
        OfferingPK pk = new OfferingPK(id, this.getProEmail(sec,
                professional));
        super.getParentPacks(ar, pk);
    }

}
