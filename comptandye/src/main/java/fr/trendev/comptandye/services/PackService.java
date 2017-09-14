/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.PackFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.ServiceFacade;
import java.util.Map;
import java.util.TreeMap;
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
public class PackService extends AbstractCommonService<Pack, OfferingPK> {

    @Inject
    PackFacade packFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ServiceFacade serviceFacade;

    private static final Logger LOG = Logger.getLogger(PackService.class.
            getName());

    public PackService() {
        super(Pack.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Pack list");
        return super.findAll(packFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(packFacade);
    }

    @Path("key")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@QueryParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        OfferingPK pk = new OfferingPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Pack : {0}", packFacade.
                prettyPrintPK(
                        pk));
        return super.find(packFacade, pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Pack entity,
            @QueryParam("professional") String professional) {

        String email;
        //TODO : remove isSecure test when using Enterprise Bean Security 
        if (sec.isSecure() && sec.isUserInRole("Professional")) {
            email = sec.getUserPrincipal().getName();
        } else {
            email = professional;
        }

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                packFacade, professionalFacade, Pack::setProfessional,
                Professional::getOfferings, e -> {
        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Pack entity,
            @QueryParam("professional") String professional) {

        Map<String, OfferingPK> pks = this.getPKs(sec, professional, new Long[]{
            entity.getId()},
                new String[]{"pk"});

        LOG.log(Level.INFO, "Updating Pack {0}", packFacade.
                prettyPrintPK(pks.get("pk")));
        return super.put(entity, packFacade, pks.get("pk"), e -> {
            e.setName(entity.getName());
            e.setPrice(entity.getPrice());
            e.setDuration(entity.getDuration());
            e.setHidden(entity.isHidden());
            e.setBusinesses(entity.getBusinesses());
        });
    }

    @Path("key")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @QueryParam("id") Long id,
            @QueryParam("professional") String professional) {

        Map<String, OfferingPK> pks = this.getPKs(sec, professional, new Long[]{
            id},
                new String[]{"pk"});

        LOG.log(Level.INFO, "Deleting Pack {0}", packFacade.
                prettyPrintPK(pks.get("pk")));
        return super.delete(packFacade, pks.get("pk"),
                e -> e.getProfessional().getOfferings().remove(e));
    }

    @Path("{packid}/addService/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addService(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        Map<String, OfferingPK> pks = this.getPKs(sec, professional, new Long[]{
            packid, offeringid},
                new String[]{"packPK", "offeringPK"});

        return super.<Service, OfferingPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                packFacade, pks.get("packPK"),
                serviceFacade,
                pks.get("offeringPK"), Service.class,
                (p, o) -> p.getOfferings().add(o));
    }

    @Path("{packid}/removeService/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeService(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        Map<String, OfferingPK> pks = this.getPKs(sec, professional, new Long[]{
            packid, offeringid},
                new String[]{"packPK", "offeringPK"});

        return super.<Service, OfferingPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                packFacade, pks.get("packPK"),
                serviceFacade,
                pks.get("offeringPK"), Service.class,
                (p, o) -> p.getOfferings().remove(o));
    }

    @Path("{packid}/addPack/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPack(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        Map<String, OfferingPK> pks = this.getPKs(sec, professional, new Long[]{
            packid, offeringid},
                new String[]{"packPK", "offeringPK"});

        return super.<Pack, OfferingPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                packFacade, pks.get("packPK"),
                packFacade,
                pks.get("offeringPK"), Pack.class,
                (p, o) -> p.getOfferings().add(o));
    }

    @Path("{packid}/removePack/offering/{offeringid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removePack(@Context SecurityContext sec,
            @PathParam("packid") Long packid,
            @PathParam("offeringid") Long offeringid,
            @QueryParam("professional") String professional) {

        Map<String, OfferingPK> pks = this.getPKs(sec, professional, new Long[]{
            packid, offeringid},
                new String[]{"packPK", "offeringPK"});

        return super.<Pack, OfferingPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                packFacade, pks.get("packPK"),
                packFacade,
                pks.get("offeringPK"), Pack.class,
                (p, o) -> p.getOfferings().remove(o));
    }

    private Map<String, OfferingPK> getPKs(SecurityContext sec,
            String professional, Long[] ids, String[] keys) {

        String proEmail;
        if (sec.isSecure() && sec.isUserInRole("Professional")) {
            proEmail = sec.getUserPrincipal().getName();
        } else {
            proEmail = professional;
        }

        Map<String, OfferingPK> map = new TreeMap<>();

        for (int i = 0; i < ids.length; i++) {
            map.put(keys[i], new OfferingPK(ids[i], proEmail));
        }

        return map;
    }
}
