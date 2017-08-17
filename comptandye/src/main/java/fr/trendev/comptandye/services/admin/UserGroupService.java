/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
@Path("/admin/user-group")
public class UserGroupService {

    @Inject
    UserGroupFacade facade;

    @Inject
    ObjectMapper om;

    private static final Logger LOG = Logger.getLogger(UserGroupService.class.
            getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing all user-groups");
        try {
            List<UserGroup> list = facade.findAll();
            LOG.log(Level.INFO, "{0} user-group", list.size());

            return Response.status(Response.Status.OK).entity(
                    om.writeValueAsString(list)).
                    build();
        } catch (Exception ex) {

            ex.printStackTrace();

            Throwable t = ExceptionHelper.
                    findRootCauseException(ex);

            String msg = MessageFormat.format(
                    "Exception occurs providing the user-group list for an administrator: {0} ; {1}",
                    new Object[]{t.getClass().toString(), t.getMessage()});

            LOG.
                    log(Level.WARNING, msg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    msg).
                    build();
        }
    }

    @GET
    @Path("{index}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUsersFromIndex(@PathParam("index") int index) {
        LOG.log(Level.INFO, "Providing the user list of the User Group at index"
                + index);

        try {
            List<UserGroup> list = facade.findAll();
            LOG.log(Level.INFO, "{0} user-group", list.size());

            if (index < 0 || index >= list.size()) {
                return Response.status(Response.Status.EXPECTATION_FAILED).
                        entity(
                                "index [" + index
                                + "] is out of range [0-"
                                + (list.size() - 1) + "]").
                        build();
            }

            return Response.status(Response.Status.OK).entity(
                    om.writeValueAsString(list.get(index).getUserAccounts())).
                    build();
        } catch (Exception ex) {

            ex.printStackTrace();

            Throwable t = ExceptionHelper.
                    findRootCauseException(ex);

            String msg = MessageFormat.format(
                    "Exception occurs providing the user-group list for an administrator: {0} ; {1}",
                    new Object[]{t.getClass().toString(), t.getMessage()});

            LOG.
                    log(Level.WARNING, msg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    msg).
                    build();
        }
    }

}
