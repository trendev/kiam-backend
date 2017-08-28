/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.AdministratorFacade;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UUIDGenerator;
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
@Path("Administrator")
public class AdministratorService {

    @Inject
    AdministratorFacade facade;

    @Inject
    UserGroupFacade userGroupFacade;

    private static final Logger LOG = Logger.getLogger(
            AdministratorService.class.
                    getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Administrator list");
        try {
            List<Administrator> list = facade.findAll();
            LOG.log(Level.INFO, "Administrator list size = {0}", list.
                    size());

            return Response.status(Response.Status.OK)
                    .entity(list).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing Administrator list to administrator");
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
            LOG.log(Level.INFO, "Total Count of Administrator = {0}", count);

            return Response.status(Response.Status.OK)
                    .entity(count).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing Administrator count to administrator");
            LOG.log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @Path("{email}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAdministrator(@PathParam("email") String email) {
        LOG.log(Level.INFO, "REST request to get Administrator : {0}", email);
        try {
            return Optional.ofNullable(facade.find(email))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            result).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Administrator "
                                    + email + " not found").build()).build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing Administrator " + email
                    + " to administrator");
            LOG.log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @Path("{email}/userGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(@PathParam("email") String email) {
        LOG.log(Level.INFO,
                "REST request to get userGroups of Administrator : {0}", email);
        try {
            return Optional.ofNullable(facade.find(email))
                    .map(result -> Response.status(Response.Status.OK).entity(
                            result.getUserGroups()).build())
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Administrator "
                                    + email + " not found").build()).build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing userGroups of " + email
                    + " to administrator");
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
    public Response post(Administrator entity) {
        LOG.log(Level.INFO, "Creating Administrator {0}", entity.getEmail());
        try {
            //generates an UUID if no one is provided
            if (entity.getUuid() == null || entity.getUuid().isEmpty()) {
                String uuid = UUIDGenerator.generate("ADMIN_", true);
                LOG.log(Level.WARNING,
                        "No UUID provided for new Administrator {0}. Generated UUID = {1}",
                        new Object[]{entity.getEmail(), uuid});
                entity.setUuid(uuid);
            }

            //encrypts the provided password
            String encrypted_pwd = PasswordGenerator.encrypt_SHA256(entity.
                    getPassword());
            entity.setPassword(encrypted_pwd);

            //adds the new administrator to the group and the group to the new admin
            UserGroup adminGroup = userGroupFacade.find("Administrator");
            adminGroup.getUserAccounts().add(entity);
            entity.getUserGroups().add(adminGroup);

            facade.create(entity);
            LOG.log(Level.INFO, "Administrator {0} created", entity.getEmail());
            return Response.created(new URI("/restapi/Administrator/" + entity.
                    getEmail())).
                    entity(entity).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs creating Administrator " + entity.
                            getEmail());
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
    public Response put(Administrator entity) {
        LOG.log(Level.INFO, "Updating Administrator {0}", entity.getEmail());
        try {
            return Optional.ofNullable(facade.find(entity.getEmail()))
                    .map(result -> {
                        result.setPassword(entity.getPassword());
                        result.setUsername(entity.getUsername());
                        result.setUuid(entity.getUuid());
                        result.setRegistrationDate(entity.getRegistrationDate());

                        facade.edit(result);
                        LOG.log(Level.INFO, "Administrator {0} updated", entity.
                                getEmail());
                        return Response.status(Response.Status.OK).entity(
                                result).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Administrator "
                                    + entity.getEmail() + " not found").build()).
                            build());
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs updating Administrator " + entity.
                            getEmail());
            LOG.
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

    @Path("{email}")
    @DELETE
    public Response delete(@PathParam("email") String email) {
        LOG.log(Level.INFO, "Deleting Administrator {0}", email);
        try {
            return Optional.ofNullable(facade.find(email))
                    .map(result -> {
                        result.getUserGroups().forEach(grp -> {
                            grp.getUserAccounts().remove(result);
                            LOG.log(Level.INFO,
                                    "Administrator {0} removed from UserGroup {1}",
                                    new Object[]{email, grp.getName()});
                        });
                        facade.remove(result);
                        LOG.log(Level.INFO, "Administrator {0} deleted", email);
                        return Response.ok().build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Administrator "
                                    + email + " not found").build()).
                            build());

        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs deleting Administrator " + email);
            LOG.
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

}
