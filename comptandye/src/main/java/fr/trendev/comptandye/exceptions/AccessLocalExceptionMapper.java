/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.exceptions;

import javax.ejb.AccessLocalException;
import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccessLocalExceptionMapper implements
        ExceptionMapper<AccessLocalException> {

    @Override
    public Response toResponse(AccessLocalException ex) {
        return Response.status(Response.Status.FORBIDDEN).entity(Json.
                createObjectBuilder().add(
                        "error", ex.getMessage()).build()).build();
    }
}
