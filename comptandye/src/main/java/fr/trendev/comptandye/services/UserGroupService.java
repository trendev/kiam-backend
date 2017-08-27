/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
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
@Path("UserGroup")
public class UserGroupService {

    @Inject
    UserGroupFacade facade;

    private static final Logger LOG = Logger.getLogger(UserGroupService.class.
            getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the UserGroup list");
        try {
            List<UserGroup> list = facade.findAll();
            LOG.log(Level.INFO, "UserGroup list size = {0}", list.
                    size());

            return Response.status(Response.Status.OK)
                    .entity(list).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing UserGroup list to administrator");
            LOG.log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {

        try {
            Long count = facade.count();
            LOG.log(Level.INFO, "Total Count of UserGroup = {0}", count);

            return Response.status(Response.Status.OK)
                    .entity(count).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing UserGroup count to administrator");
            LOG.log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroup(@PathParam("name") String name) {
        LOG.log(Level.INFO, "REST request to get UserGroup : {0}", name);
        try {
            return Optional.ofNullable(facade.find(name))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            result).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error", "UserGroup "
                                    + name + " not found").build()).build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing UserGroup " + name
                    + " to administrator");
            LOG.log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @Path("{name}/userAccounts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAccounts(@PathParam("name") String name) {
        LOG.log(Level.INFO,
                "REST request to get userAccounts of UserGroup : {0}", name);
        try {
            return Optional.ofNullable(facade.find(name))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            result.getUserAccounts()).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error", "UserGroup "
                                    + name + " not found").build()).build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing userAccounts of user-group to administrator");
            LOG.
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(UserGroup entity) {
        LOG.log(Level.INFO, "Creating UserGroup {0}", entity.getName());
        try {
            facade.create(entity);
            LOG.log(Level.INFO, "UserGroup {0} created", entity.getName());
            return Response.created(new URI("/restapi/UserGroup/" + entity.
                    getName())).
                    entity(entity).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs creating UserGroup " + entity.getName());
            LOG.
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(UserGroup entity) {
        LOG.log(Level.INFO, "Updating UserGroup {0}", entity.getName());
        try {
            return Optional.ofNullable(facade.find(entity.getName()))
                    .map(result -> {
                        result.setDescription(entity.getDescription());
                        facade.edit(result);
                        LOG.log(Level.INFO, "UserGroup {0} updated", entity.
                                getName());
                        return Response.status(Response.Status.OK).entity(
                                result).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error", "UserGroup "
                                    + entity.getName() + " not found").build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs updating UserGroup " + entity);
            LOG.
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @Path("{name}")
    @DELETE
    public Response delete(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Deleting UserGroup {0}", name);
        try {
            facade.remove(facade.find(name));
            LOG.log(Level.INFO, "UserGroup {0} deleted", name);
            return Response.ok().
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs deleting UserGroup " + name);
            LOG.
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

}
