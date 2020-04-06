/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.customerdetails.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.customerdetails.entities.CustomerDetails;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.customerdetails.controllers.CustomerDetailsFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
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
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("CustomerDetails")
@RolesAllowed({"Administrator"})
public class CustomerDetailsService extends AbstractCommonService<CustomerDetails, String> {

    @Inject
    CustomerDetailsFacade customerDetailsFacade;

    private final Logger LOG = Logger.getLogger(
            CustomerDetailsService.class.
                    getName());

    public CustomerDetailsService() {
        super(CustomerDetails.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<CustomerDetails, String> getFacade() {
        return customerDetailsFacade;
    }

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

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("id") String id,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get CustomerDetails : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(CustomerDetails entity) {
        LOG.log(Level.INFO, "Creating CustomerDetails {0}", super.stringify(
                entity));

        return super.post(entity, e -> {
            e.setId(UUIDGenerator.generateID());
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(CustomerDetails entity) {
        LOG.log(Level.INFO, "Updating CustomerDetails {0}", entity.getId());
        return super.put(entity, entity.getId(),
                e -> {
                    e.setFirstName(entity.getFirstName());
                    e.setLastName(entity.getLastName());
                    e.setJobrole(entity.getJobrole());
                    e.setNickname(entity.getNickname());
                    e.setPhone(entity.getPhone());
                    e.setBirthdate(entity.getBirthdate());
                    e.setSex(entity.getSex());
                    e.setPicturePath(entity.getPicturePath());
                    e.setComments(entity.getComments());
                });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") String id) {
        LOG.log(Level.INFO, "Deleting CustomerDetails {0}", id);
        return super.delete(id, e -> {
        });
    }

}
