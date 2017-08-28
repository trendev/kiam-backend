/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

//Uncomment in order to provide this ExceptionMapper
@Provider
public class DebugExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger logger = Logger.getLogger(
            DebugExceptionMapper.class.getName());

    @Override
    public Response toResponse(Exception ex) {
        logger.
                log(Level.WARNING, "DebugExceptionMapper: " + ex.getMessage(),
                        ex);
        String errmsg = ExceptionHelper.handleException(ex,
                "DebugExceptionMapper caught an Exception");
        return Response.status(Response.Status.EXPECTATION_FAILED).entity(Json.
                createObjectBuilder().add(
                        "error", errmsg).build()).build();
    }
}
