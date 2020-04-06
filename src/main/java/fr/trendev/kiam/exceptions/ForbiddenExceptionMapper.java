/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.exceptions;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements
        ExceptionMapper<ForbiddenException> {

    private static final Logger LOG = Logger.getLogger(
            ForbiddenExceptionMapper.class.getName());

    @Override
    public Response toResponse(ForbiddenException ex) {
        LOG.log(Level.INFO, "UNAUTHORIZED_USER : " + ex.getMessage());
        return Response.status(Response.Status.UNAUTHORIZED).entity(Json.
                createObjectBuilder().add("error", ex.getMessage()).build()).
                build();
    }
}
