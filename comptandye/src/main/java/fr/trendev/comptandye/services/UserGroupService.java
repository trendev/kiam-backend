/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.GET;
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
@Path("UserGroup")
public class UserGroupService extends CommonRestService<UserGroup, String> {

    @Inject
    UserGroupFacade facade;

    @Inject
    Logger logger;

    public UserGroupService() {
        super("UserGroup");
    }

    @Override
    protected UserGroupFacade getFacade() {
        return facade;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroup(@PathParam("name") String name) {
        logger.log(Level.INFO, "REST request to get UserGroup : {0}", name);
        return super.find(name);
    }

    @Path("{name}/userAccounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAccounts(@PathParam("name") String name) {
        logger.log(Level.INFO,
                "REST request to get userAccounts of UserGroup : {0}", name);
        try {
            return Optional.ofNullable(facade.find(name))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            result.getUserAccounts()).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createArrayBuilder().build()).build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing userAccounts of user-group to administrator");
            getLogger().
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }
}
