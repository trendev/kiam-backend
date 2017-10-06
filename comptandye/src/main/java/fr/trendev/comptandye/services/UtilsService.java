/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.utils.PasswordGenerator;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Utils")
@PermitAll
public class UtilsService {

    private static final Logger LOG = Logger.getLogger(UtilsService.class.
            getName());

    @Path("password")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String find(@QueryParam("size") int id) {
        return PasswordGenerator.autoGenerate(id);
    }

}
