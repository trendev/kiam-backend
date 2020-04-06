/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.usergroup.boundaries;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.useraccount.entities.UserAccount;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.usergroup.controllers.UserGroupFacade;
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
@Path("UserGroup")
@RolesAllowed({"Administrator"})
public class UserGroupService extends AbstractCommonService<UserGroup, String> {

    @Inject
    UserGroupFacade userGroupFacade;

    private final Logger LOG = Logger.getLogger(UserGroupService.class.
            getName());

    public UserGroupService() {
        super(UserGroup.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<UserGroup, String> getFacade() {
        return userGroupFacade;
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

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("name") String name,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get UserGroup : {0}", name);
        return super.find(name, refresh);
    }

    @Path("{name}/userAccounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getUserAccounts(@Suspended final AsyncResponse ar, @PathParam(
            "name") String name) {
        super.provideRelation(ar, name,
                UserGroup::getUserAccounts, UserAccount.class);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(UserGroup payload) {
        LOG.log(Level.INFO, "Creating UserGroup {0}", payload.getName());
        return super.post(payload, e -> {
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(UserGroup payload) {
        LOG.log(Level.INFO, "Updating UserGroup {0}", payload.getName());
        return super.put(payload, payload.getName(), e -> {
            e.setDescription(payload.getDescription());
        });
    }

    @Path("{name}")
    @DELETE
    public Response delete(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Deleting UserGroup {0}", name);
        return super.delete(name, e -> {
            e.getUserAccounts().forEach(u -> {
                u.getUserGroups().remove(e);
                LOG.log(Level.INFO,
                        "User {0} removed from UserGroup {1}",
                        new Object[]{u.getEmail(), name});
            });
        });
    }

}
