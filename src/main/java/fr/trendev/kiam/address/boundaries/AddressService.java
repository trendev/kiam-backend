/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.address.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.address.entities.Address;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.address.controllers.AddressFacade;
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
@Path("Address")
@RolesAllowed({"Administrator"})
public class AddressService extends AbstractCommonService<Address, String> {

    @Inject
    AddressFacade addressFacade;

    private final Logger LOG = Logger.getLogger(
            AddressService.class.
                    getName());

    public AddressService() {
        super(Address.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Address, String> getFacade() {
        return addressFacade;
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
        LOG.log(Level.INFO, "REST request to get Address : {0}", id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Address entity) {
        LOG.log(Level.INFO, "Creating Address {0}", super.stringify(entity));
        return super.post(entity, e -> {
            e.setId(UUIDGenerator.generateID());
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Address entity) {
        LOG.log(Level.INFO, "Updating Address {0}", entity.getId());
        return super.put(entity, entity.getId(),
                e -> {
            e.setStreet(entity.getStreet());
            e.setOptional(entity.getOptional());
            e.setPostalCode(entity.getPostalCode());
            e.setCity(entity.getCity());
            e.setCountry(entity.getCountry());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") String id) {
        LOG.log(Level.INFO, "Deleting Address {0}", id);
        return super.delete(id, e -> {
        });
    }

}
