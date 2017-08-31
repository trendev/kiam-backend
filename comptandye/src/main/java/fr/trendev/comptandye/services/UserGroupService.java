/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
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
@Path("UserGroup")
public class UserGroupService extends AbstractCommonService<UserGroup, String> {

    @Inject
    UserGroupFacade userGroupFacade;

    private static final Logger LOG = Logger.getLogger(UserGroupService.class.
            getName());

    public UserGroupService() {
        super(UserGroup.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the UserGroup list");
        return super.findAll(userGroupFacade, facade -> facade.findAll());
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(userGroupFacade);
    }

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("name") String name,
            @QueryParam("refresh") Boolean refresh) {
        LOG.log(Level.INFO, "REST request to get UserGroup : {0}", name);
        return super.find(userGroupFacade, name, refresh);
    }

    @Path("{name}/userAccounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAccounts(@PathParam("name") String name) {
        LOG.log(Level.INFO,
                "REST request to get userAccounts of UserGroup : {0}", name);
        return super.provideRelation(userGroupFacade, name,
                UserGroup::getUserAccounts);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(UserGroup entity) {
        LOG.log(Level.INFO, "Creating UserGroup {0}", entity.getName());
        return super.post(entity, userGroupFacade, e -> {
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(UserGroup entity) {
        LOG.log(Level.INFO, "Updating UserGroup {0}", entity.getName());
        return super.put(entity, userGroupFacade, entity.getName(), e -> {
            e.setDescription(entity.getDescription());
        });
    }

    @Path("{name}")
    @DELETE
    public Response delete(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Deleting UserGroup {0}", name);
        return super.delete(userGroupFacade, name, e -> {
            e.getUserAccounts().forEach(u -> {
                u.getUserGroups().remove(e);
                LOG.log(Level.INFO,
                        "User {0} removed from UserGroup {1}",
                        new Object[]{u.getEmail(), name});
            });
        });
    }

}
