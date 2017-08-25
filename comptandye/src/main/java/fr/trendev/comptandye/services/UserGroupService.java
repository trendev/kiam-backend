/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 *
 * @author jsie
 */
@Stateless
@Path("user-group")
public class UserGroupService extends CommonRestService<UserGroup, String> {

    @Inject
    UserGroupFacade facade;

    public UserGroupService() {
        super("user-group", Logger.getLogger(UserGroupService.class.getName()));

    }

    @Override
    protected UserGroupFacade getFacade() {
        return facade;
    }

//    @GET
//    @Path("{index}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response findUsersFromIndex(@PathParam("index") int index) {
//        LOG.log(Level.INFO,
//                "Providing the user list of the User Group at index ["
//                + index + "]");
//
//        try {
//            List<UserGroup> list = facade.findAll();
//
//            if (index < 0 || index >= list.size()) {
//                String msg = "index [" + index
//                        + "] is out of range [0-"
//                        + (list.size() - 1) + "]";
//                return Response.status(Response.Status.EXPECTATION_FAILED).
//                        entity(
//                                Json.createObjectBuilder().add("error", msg).
//                                        build()).
//                        build();
//            }
//
//            return Response.status(Response.Status.OK)
//                    .entity(list.get(index).getUserAccounts()).
//                    build();
//        } catch (Exception ex) {
//
//            ex.printStackTrace();
//
//            Throwable t = ExceptionHelper.
//                    findRootCauseException(ex);
//
//            String msg = MessageFormat.format(
//                    "Exception occurs providing the user-group list for an administrator: {0} ; {1}",
//                    new Object[]{t.getClass().toString(), t.getMessage()});
//
//            LOG.
//                    log(Level.WARNING, msg);
//            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
//                    Json.createObjectBuilder().add("error", msg).build()).
//                    build();
//        }
//    }
}
