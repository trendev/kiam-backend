/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.offering.boundaries;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.exceptions.ExceptionHelper;
import fr.trendev.comptandye.offering.entities.Offering;
import fr.trendev.comptandye.pack.controllers.PackFacade;
import fr.trendev.comptandye.pack.entities.Pack;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.sale.controllers.SaleFacade;
import fr.trendev.comptandye.security.controllers.AuthenticationHelper;
import fr.trendev.comptandye.service.controllers.ServiceFacade;
import fr.trendev.comptandye.service.entities.Service;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("OfferingsModel")
@RolesAllowed({"Administrator", "Professional"})
public class OfferingsModelService {

    @Inject
    ObjectMapper om;

    @Inject
    PackFacade packFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ServiceFacade serviceFacade;

    @Inject
    SaleFacade saleFacade;

    @Inject
    AuthenticationHelper authenticationHelper;

    private final Logger LOG = Logger.getLogger(OfferingsModelService.class.
            getName());

    @Path("build")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response build(@Context SecurityContext sec,
            @QueryParam("professional") String professional) {

        String email = authenticationHelper.
                getProEmail(sec, professional);
        LOG.log(Level.INFO,
                "Generating pre-build Offerings for the professional [{0}]",
                email);

        return Optional.ofNullable(professionalFacade.find(email))
                .map(pro -> {
                    try {

                        List<Offering> offeringsModel = new LinkedList<>();

                        for (Business b : pro.getBusinesses()) {
                            String business = b.getDesignation().toLowerCase();

                            Map<String, Offering> services = this.
                                    importServices(pro, business);

                            List<Offering> packs = this.importPacks(pro,
                                    business,
                                    services);

                            offeringsModel.addAll(services.values());
                            offeringsModel.addAll(packs);

                        }

                        //avoid to return pro.getOffering(): can be outdated offering!
                        return Response.created(
                                new URI("Professional/offerings"))
                                .entity(offeringsModel)
                                .build();
                    } catch (Exception ex) {
                        String errmsg = ExceptionHelper.handleException(ex,
                                "Error occurs Generating pre-build Offerings for the professional "
                                + email);
                        LOG.log(Level.WARNING, errmsg, ex);
                        throw new WebApplicationException(errmsg, ex);
                    }
                })
                .orElse(Response.status(Status.NOT_FOUND).entity(Json.
                        createObjectBuilder()
                        .add("error", "Professional " + email + " not found")
                        .build())
                        .build());
    }

    /**
     * Reads and imports Services from a JSON file
     *
     * @param pro the future owner of the services
     * @return a Map of the newly created Services
     * @throws IOException if an error occurs reading/parsing the file
     */
    private Map<String, Offering> importServices(Professional pro,
            String business) throws IOException {

        ClassLoader classloader = Thread.currentThread().
                getContextClassLoader();
        String path = "json/services_" + business + ".json";

        try (InputStream is = classloader.getResourceAsStream(path)) {

            Map<String, Offering> map = new TreeMap<>();

            LOG.log(Level.INFO, "Reading in {0}", path);
            Arrays.asList(om.readValue(is, Service[].class)).stream()
                    .map(s -> {
                        s.setProfessional(pro);
                        pro.getOfferings().add(s);
                        return s;
                    })
                    .forEach(s -> {
                        serviceFacade.create(s);
                        //map the managed entity
                        map.put(s.getName(), s);
                    });

            return map;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE,
                    "Exception occurs parsing services from file : {0}", path);
            throw new IOException(ex);
        }

    }

    /**
     * Reads and imports Packs from a JSON file
     *
     * @param pro the future owner of the Packs
     * @param services the Services Map, previously imported
     * @throws IOException if an error occurs reading/parsing the file
     */
    private List<Offering> importPacks(Professional pro,
            String business,
            Map<String, Offering> services)
            throws IOException {

        ClassLoader classloader = Thread.currentThread().
                getContextClassLoader();

        String path = "json/packs_" + business + ".json";

        try (InputStream is = classloader.getResourceAsStream(path)) {
            LOG.log(Level.INFO, "Reading in {0}", path);
            List<Offering> packs = Arrays.asList(om.readValue(is, Pack[].class))
                    .stream()
                    .map(p -> {
                        List<Offering> offerings = p.getOfferings().stream()
                                //use the managed entity instead of the provided offering
                                .map(o -> Optional.ofNullable(
                                        services.get(o.getName()))
                                        .map(_o -> {
                                            //update the link between the offering and the pack
                                            _o.getParentPacks().add(p);
                                            return _o;
                                        })
                                        //occurs if json file contains errors
                                        .orElseThrow(() ->
                                                new WebApplicationException(
                                                        "Service " + o.getName()
                                                        + " not found !"))
                                )
                                .collect(Collectors.toList());

                        p.setOfferings(offerings);

                        p.setProfessional(pro);
                        pro.getOfferings().add(p);
                        packFacade.create(p);
                        return p;
                    })
                    .collect(Collectors.toList());

            return packs;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE,
                    "Exception occurs parsing packs from file : {0}", path);
            throw new IOException(ex);
        }

    }

}
