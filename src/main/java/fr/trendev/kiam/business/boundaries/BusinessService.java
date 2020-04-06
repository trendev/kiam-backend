/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.business.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.business.entities.Business;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.business.controllers.BusinessFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Business")
@RolesAllowed({"Administrator"})
public class BusinessService extends AbstractCommonService<Business, String> {

    @Inject
    BusinessFacade businessFacade;

    private final Logger LOG = Logger.getLogger(BusinessService.class.
            getName());

    public BusinessService() {
        super(Business.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Business, String> getFacade() {
        return businessFacade;
    }

    @RolesAllowed({"Administrator", "Professional"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
    }

    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @Path("{designation}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("designation") String designation,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get Business : {0}", designation);
        return super.find(designation, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Business entity) {
        LOG.log(Level.INFO, "Creating Business {0}", super.stringify(
                entity));

        return super.post(entity, e -> {
        });
    }

    @Path("{designation}")
    @DELETE
    public Response delete(@PathParam("designation") String designation) {
        LOG.log(Level.INFO, "Deleting Business {0}", designation);
        return super.delete(designation, e -> {
        });
    }

}
