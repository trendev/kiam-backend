/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.ServiceFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("Service")
public class ServiceService extends AbstractCommonService<Service, OfferingPK> {

    @Inject
    ServiceFacade serviceFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    private static final Logger LOG = Logger.getLogger(ServiceService.class.
            getName());

    public ServiceService() {
        super(Service.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Service list");
        return super.findAll(serviceFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(serviceFacade);
    }

    @Path("key")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@QueryParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        OfferingPK pk = new OfferingPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Service : {0}", serviceFacade.
                prettyPrintPK(
                        pk));
        return super.find(serviceFacade, pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Service entity,
            @QueryParam("professional") String professional) {
        //TODO : remove isSecure test when using Enterprise Bean Security 
        if (sec.isSecure() && sec.isUserInRole("Professional")) {
            return super.<Professional, String>post(entity, sec.
                    getUserPrincipal().getName(),
                    AbstractFacade::prettyPrintPK,
                    Professional.class,
                    serviceFacade, professionalFacade, Service::setProfessional,
                    Professional::getOfferings, e -> {
            });
        } else {
            return super.<Professional, String>post(entity, professional,
                    AbstractFacade::prettyPrintPK,
                    Professional.class,
                    serviceFacade, professionalFacade, Service::setProfessional,
                    Professional::getOfferings, e -> {
            });
        }
    }

//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response put(Service entity) {
//        LOG.log(Level.INFO, "Updating Service {0}", entity.getId());
//        return super.put(entity, serviceFacade, entity.getId(),
//                e -> {
//            e.setStreet(entity.getStreet());
//            e.setOptional(entity.getOptional());
//            e.setPostalCode(entity.getPostalCode());
//            e.setCity(entity.getCity());
//            e.setCountry(entity.getCountry());
//        });
//    }
//
    @Path("key")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @QueryParam("id") Long id,
            @QueryParam("professional") String professional) {
        LOG.log(Level.INFO, "Deleting Service {0}", id);
        if (sec.isSecure() && sec.isUserInRole("Professional")) {
            return super.delete(serviceFacade, new OfferingPK(id, sec.
                    getUserPrincipal().getName()),
                    e -> e.getProfessional().getOfferings().remove(e));
        } else {
            return super.delete(serviceFacade, new OfferingPK(id, professional),
                    e -> e.getProfessional().getOfferings().remove(e));
        }
    }
}
