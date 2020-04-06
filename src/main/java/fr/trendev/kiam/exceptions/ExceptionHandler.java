/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Providers;

/**
 *
 * @author jsie
 */
@Stateless
public class ExceptionHandler {

    @Context
    Providers providers;

    private static final Logger LOG = Logger.getLogger(ExceptionHandler.class.
            getName());

    @SuppressWarnings("unchecked")
    public Response handle(Throwable t) {

        LOG.log(Level.INFO, "ExceptionHandler caught {0} : {1}", new Object[]{
            t.getMessage(), t});

        Throwable cause = t.getCause();

        if (providers != null && cause != null) {
            ExceptionMapper mapper =
                    providers.getExceptionMapper(cause.getClass());
            if (mapper != null) {
                return mapper.toResponse(cause);
            }
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                entity(cause).build();
    }

}
