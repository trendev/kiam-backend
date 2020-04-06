/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OverallExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(
            OverallExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        LOG.
                log(Level.WARNING, OverallExceptionMapper.class.getSimpleName()
                        + ": " + ex.getMessage(),
                        ex);
        String errmsg = ExceptionHelper.handleException(ex,
                OverallExceptionMapper.class.getSimpleName()
                + " caught an Exception");

        if (!errmsg.contains(ex.getMessage())) {
            errmsg = ex.getMessage() + " / " + errmsg;
        }

        return Response.status(Response.Status.EXPECTATION_FAILED).entity(Json.
                createObjectBuilder().add(
                        "error", errmsg).build()).
                build();
    }
}
