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
@Path("user-group")
public class UserGroupService extends CommonRestService<UserGroup, String> {

    @Inject
    UserGroupFacade facade;

    @Inject
    Logger log;

    public UserGroupService() {
        super("user-group");
    }

    @Override
    protected UserGroupFacade getFacade() {
        return facade;
    }

    @Override
    protected Logger getLogger() {
        return log;
    }

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroup(@PathParam("name") String name) {
//        log.debug("REST request to get UserGroup : {}", name);
        return super.find(name);
    }

}
