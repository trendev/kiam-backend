/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import javax.json.Json;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements
        ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException ex) {
        return Response.status(Response.Status.BAD_REQUEST).entity(Json.
                createObjectBuilder().add("error", ex.getMessage()).build()).
                build();
    }
}
