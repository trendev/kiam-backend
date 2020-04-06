/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.exceptions;

import javax.json.Json;
import javax.transaction.RollbackException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RollbackExceptionMapper
        implements ExceptionMapper<RollbackException> {

    @Override
    public Response toResponse(RollbackException exception) {

        Throwable cause = ExceptionHelper.findRootCauseException(exception);

        if (cause instanceof ConstraintViolationException) {
            return ExceptionHelper.handle((ConstraintViolationException) cause);
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(Json.
                createObjectBuilder().add(
                        "error",
                        (cause.getMessage() != null)
                        ? cause.getMessage()
                        : cause.toString()).
                build()).
                build();

    }

}
