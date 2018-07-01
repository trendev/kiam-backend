/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import java.util.logging.Logger;
import javax.json.Json;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements
        ExceptionMapper<WebApplicationException> {

    private static final Logger LOG = Logger.getLogger(
            WebApplicationExceptionMapper.class.getName());

    @Override
    public Response toResponse(WebApplicationException ex) {

        String errmsg = ExceptionHelper.handleException(ex,
                WebApplicationExceptionMapper.class.getSimpleName()
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
