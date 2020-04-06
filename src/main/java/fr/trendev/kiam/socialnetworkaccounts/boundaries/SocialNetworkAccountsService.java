/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.socialnetworkaccounts.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.socialnetworkaccounts.controllers.SocialNetworkAccountsFacade;
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
@Path("SocialNetworkAccounts")
@RolesAllowed({"Administrator"})
public class SocialNetworkAccountsService extends AbstractCommonService<SocialNetworkAccounts, String> {

    @Inject
    SocialNetworkAccountsFacade socialNetworkAccountsFacade;

    private final Logger LOG = Logger.getLogger(
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
    protected AbstractFacade<SocialNetworkAccounts, String> getFacade() {
        return socialNetworkAccountsFacade;
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
        LOG.log(Level.INFO, "REST request to get SocialNetworkAccounts : {0}",
                id);
        return super.find(id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(SocialNetworkAccounts payload) {
        LOG.log(Level.INFO, "Creating SocialNetworkAccounts {0}", super.
                stringify(payload));

        return super.post(payload, e -> {
            e.setId(UUIDGenerator.generateID());
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(SocialNetworkAccounts payload) {
        LOG.
                log(Level.INFO, "Updating SocialNetworkAccounts {0}", payload.
                        getId());
        return super.put(payload, payload.getId(),
                e -> {
                    e.setFacebook(payload.getFacebook());
                    e.setTwitter(payload.getTwitter());
                    e.setInstagram(payload.getInstagram());
                    e.setPinterest(payload.getPinterest());
                });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") String id) {
        LOG.log(Level.INFO, "Deleting SocialNetworkAccounts {0}", id);
        return super.delete(id, e -> {
        });
    }
}
