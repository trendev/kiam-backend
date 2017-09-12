/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Business;
import fr.trendev.comptandye.sessions.BusinessFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Business")
public class BusinessService extends AbstractCommonService<Business, String> {

    @Inject
    BusinessFacade businessFacade;

    private static final Logger LOG = Logger.getLogger(BusinessService.class.
            getName());

    public BusinessService() {
        super(Business.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected String prettyPrintPK(String pk) {
        return pk;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Business list");
        return super.
                findAll(businessFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(businessFacade);
    }

    @Path("{designation}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("designation") String designation,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get Business : {0}", designation);
        return super.find(businessFacade, designation, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Business entity) {
        LOG.log(Level.INFO, "Creating Business {0}", super.stringify(
                entity));

        return super.post(entity, businessFacade, e -> {
        });
    }

    @Path("{designation}")
    @DELETE
    public Response delete(@PathParam("designation") String designation) {
        LOG.log(Level.INFO, "Deleting Business {0}", designation);
        return super.delete(businessFacade, designation, e -> {
        });
    }

}
