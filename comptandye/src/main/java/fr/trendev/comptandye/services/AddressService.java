/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.sessions.AddressFacade;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Address")
public class AddressService extends AbstractCommonService<Address, Long> {

    @Inject
    AddressFacade addressFacade;

    private static final Logger LOG = Logger.getLogger(
            AdministratorService.class.
                    getName());

    public AddressService() {
        super(Address.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Address list");
        return super.findAll(addressFacade, facade -> facade.findAll());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(addressFacade);
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "REST request to get Address : {0}", id);
        return super.find(addressFacade, id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Address entity) {
        LOG.log(Level.INFO, "Creating Address {0}", super.stringify(entity));

        return super.post(entity, addressFacade, e -> {
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Address entity) {
        LOG.log(Level.INFO, "Updating Address {0}", entity.getId());
        return super.put(entity, addressFacade, entity.getId(), e -> {
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting Address {0}", id);
        return super.delete(addressFacade, id, e -> {
        });
    }
}
