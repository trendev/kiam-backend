/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.PackFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
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

        OfferingPK pk;

        if (sec.isSecure() && sec.isUserInRole("Professional")) {
            pk = new OfferingPK(entity.getId(), sec.
                    getUserPrincipal().getName());
        } else {
            pk = new OfferingPK(entity.getId(), professional);
        }

        LOG.log(Level.INFO, "Updating Pack {0}", packFacade.
                prettyPrintPK(pk));
        return super.put(entity, packFacade, pk, e -> {
            e.setName(entity.getName());
            e.setPrice(entity.getPrice());
            e.setDuration(entity.getDuration());
            e.setHidden(entity.isHidden());
        });
    }

    @Path("key")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @QueryParam("id") Long id,
            @QueryParam("professional") String professional) {

        OfferingPK pk;

        if (sec.isSecure() && sec.isUserInRole("Professional")) {
            pk = new OfferingPK(id, sec.
                    getUserPrincipal().getName());
        } else {
            pk = new OfferingPK(id, professional);
        }

        LOG.log(Level.INFO, "Deleting Pack {0}", packFacade.
                prettyPrintPK(pk));
        return super.delete(packFacade, pk,
                e -> e.getProfessional().getOfferings().remove(e));
    }

//    @Path("hide/key")
//    @PUT
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response hide(@Context SecurityContext sec,
//            @QueryParam("id") Long id,
//            @QueryParam("professional") String professional) {
//        OfferingPK pk;
//
//        if (sec.isSecure() && sec.isUserInRole("Professional")) {
//            pk = new OfferingPK(id, sec.
//                    getUserPrincipal().getName());
//        } else {
//            pk = new OfferingPK(id, professional);
//        }
//
//        LOG.log(Level.INFO, "Hiding Service {0} from Professional {1}",
//                new Object[]{serviceFacade.
//                            prettyPrintPK(pk), pk.getProfessional()});
//        //TODO : use a recursive process to delete service from the entire Professional's offering
//        return super.<Professional, String>manageAssociation(
//                AssociationManagementEnum.REMOVE,
//                serviceFacade, pk,
//                professionalFacade,
//                pk.getProfessional(), Professional.class,
//                (s, p) -> p.getOfferings().remove(s));
//    }
}
