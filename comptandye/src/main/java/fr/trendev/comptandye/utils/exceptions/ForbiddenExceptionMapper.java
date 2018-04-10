/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import javax.json.Json;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements
        ExceptionMapper<ForbiddenException> {

    @Override
    public Response toResponse(ForbiddenException ex) {
        return Response.status(Response.Status.UNAUTHORIZED).entity(Json.
                createObjectBuilder().add("error", ex.getMessage()).build()).
                build();
    }
}
