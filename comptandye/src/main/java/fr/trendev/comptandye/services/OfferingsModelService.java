/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Pack")
@RolesAllowed({"Administrator", "Professional"})
public class OfferingsModelService {
//
//    @Inject
//    ObjectMapper om;
//
//    @Inject
//    PackFacade packFacade;
//
//    @Inject
//    ProfessionalFacade professionalFacade;
//
//    @Inject
//    ServiceFacade serviceFacade;
//
//    @Inject
//    SaleFacade saleFacade;
//
//    private final Logger LOG = Logger.getLogger(OfferingsModelService.class.
//            getName());
//
//    @Path("buildModelOfferings")
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response buildModelOfferings(@Context SecurityContext sec,
//            @QueryParam("professional") String professional) {
//
//        String email = super.getProEmail(sec, professional);
//        LOG.log(Level.INFO,
//                "Generating pre-build Offerings for the professional [{0}]",
//                email);
//
//        return Optional.ofNullable(professionalFacade.find(email))
//                .map(pro -> {
//                    try {
//                        Map<String, Offering> services = this.
//                                importServices(pro);
//                        List<Offering> packs = this.importPacks(pro, services);
//
//                        //concat pack and services
//                        packs.addAll(services.values());
//
//                        //avoid to return pro.getOffering(): can be outdated offering!
//                        return Response.created(
//                                new URI("Professional/offerings"))
//                                .entity(packs)
//                                .build();
//                    } catch (Exception ex) {
//                        String errmsg = ExceptionHelper.handleException(ex,
//                                "Error occurs Generating pre-build Offerings for the professional "
//                                + email);
//                        getLogger().log(Level.WARNING, errmsg, ex);
//                        throw new WebApplicationException(errmsg, ex);
//                    }
//                })
//                .orElse(Response.status(Status.NOT_FOUND).entity(Json.
//                        createObjectBuilder()
//                        .add("error", "Professional " + email + " not found")
//                        .build())
//                        .build());
//    }
//
//    /**
//     * Reads and imports Services from a JSON file
//     *
//     * @param pro the future owner of the services
//     * @return a Map of the newly created Services
//     * @throws IOException if an error occurs reading/parsing the file
//     */
//    private Map<String, Offering> importServices(Professional pro) throws
//            IOException {
//
//        ClassLoader classloader = Thread.currentThread().
//                getContextClassLoader();
//        InputStream is = classloader.getResourceAsStream(
//                "json/services_hairdressing.json");
//
//        Map<String, Offering> map = new TreeMap<>();
//
//        Arrays.asList(om.readValue(is, Service[].class)).stream()
//                .map(s -> {
//                    s.setProfessional(pro);
//                    pro.getOfferings().add(s);
//                    return s;
//                })
//                .forEach(s -> {
//                    serviceFacade.create(s);
//                    //map the managed entity
//                    map.put(s.getName(), s);
//                });
//
//        is.close();
//
//        return map;
//    }
//
//    /**
//     * Reads and imports Packs from a JSON file
//     *
//     * @param pro the future owner of the Packs
//     * @param services the Services Map, previously imported
//     * @throws IOException if an error occurs reading/parsing the file
//     */
//    private List<Offering> importPacks(Professional pro,
//            Map<String, Offering> services)
//            throws IOException {
//        ClassLoader classloader = Thread.currentThread().
//                getContextClassLoader();
//        InputStream is = classloader.getResourceAsStream(
//                "json/packs_hairdressing.json");
//
//        List<Offering> packs = Arrays.asList(om.readValue(is, Pack[].class)).
//                stream()
//                .map(p -> {
//                    List<Offering> offerings = p.getOfferings().stream()
//                            //use the managed entity instead of the provided offering
//                            .map(o -> Optional.ofNullable(
//                                    services.get(o.getName()))
//                                    .map(_o -> {
//                                        //update the link between the offering and the pack
//                                        _o.getParentPacks().add(p);
//                                        return _o;
//                                    })
//                                    //occurs if json file contains errors
//                                    .orElseThrow(() ->
//                                            new WebApplicationException(
//                                                    "Service " + o.getName()
//                                                    + " not found !"))
//                            )
//                            .collect(Collectors.toList());
//
//                    p.setOfferings(offerings);
//
//                    p.setProfessional(pro);
//                    pro.getOfferings().add(p);
//                    packFacade.create(p);
//                    return p;
//                })
//                .collect(Collectors.toList());
//
//        is.close();
//
//        return packs;
//    }

}
