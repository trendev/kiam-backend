/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.SocialNetworkAccountsFacade;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("SocialNetworkAccounts")
public class SocialNetworkAccountsService extends AbstractCommonService<SocialNetworkAccounts, Long> {

    @Inject
    SocialNetworkAccountsFacade socialNetworkAccountsFacade;

    private static final Logger LOG = Logger.getLogger(
            SocialNetworkAccountsService.class.
                    getName());

    public SocialNetworkAccountsService() {
        super(SocialNetworkAccounts.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<SocialNetworkAccounts, Long> getFacade() {
        return socialNetworkAccountsFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the SocialNetworkAccounts list");
        return super.findAll();
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
    public Response find(@PathParam("id") Long id,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get SocialNetworkAccounts : {0}",
                id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(SocialNetworkAccounts entity) {
        LOG.log(Level.INFO, "Creating SocialNetworkAccounts {0}", super.
                stringify(entity));

        return super.post(entity, e -> {
            e.setId(null);
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(SocialNetworkAccounts entity) {
        LOG.
                log(Level.INFO, "Updating SocialNetworkAccounts {0}", entity.
                        getId());
        return super.put(entity, entity.getId(),
                e -> {
            e.setFacebook(entity.getFacebook());
            e.setTwitter(entity.getTwitter());
            e.setInstagram(entity.getInstagram());
            e.setPinterest(entity.getPinterest());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting SocialNetworkAccounts {0}", id);
        return super.delete(id, e -> {
        });
    }
}
