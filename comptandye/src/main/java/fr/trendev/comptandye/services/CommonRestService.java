/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public abstract class CommonRestService<E, P> {

    private final String path;
    private final Logger LOG;

    public CommonRestService(String path, Logger LOG) {
        this.path = path;
        this.LOG = LOG;
    }

    protected abstract AbstractFacade<E, P> getFacade();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the {0} list", path);
        try {
            List<E> list = this.getFacade().findAll();
            LOG.log(Level.INFO, "{0} list size = {1}", new Object[]{path, list.
                size()});

            return Response.status(Response.Status.OK)
                    .entity(list).
                    build();
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs providing the " + path
                    + " list for an administrator");
            LOG.
                    log(Level.WARNING, errmsg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder().add("error", errmsg).build()).
                    build();
        }
    }

}
