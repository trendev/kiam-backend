/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.exceptions;

import java.text.MessageFormat;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public class ExceptionHelper {

    @SuppressWarnings("empty-statement")
    static Throwable findRootCauseException(Throwable e) {
        Throwable t = e;
        for (; t.getCause() != null; t = t.getCause());
        return t;
    }

    public final static String handleException(Throwable ex, String message) {

        Throwable t = findRootCauseException(ex);

        String errmsg = MessageFormat.format(
                "{0}: {1} ; {2}",
                new Object[]{message, t.getClass().toString(), t.getMessage()});

        return errmsg;

    }

    final static Response handle(ConstraintViolationException exception) {

        JsonArrayBuilder jab = Json.createArrayBuilder();

        ((ConstraintViolationException) exception).getConstraintViolations()
                .stream()
                .forEach(cv -> aggregateViolations(jab, cv));

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(
                        Json.createObjectBuilder()
                                .add("error",
                                        Json.createObjectBuilder()
                                                .add("constraint-violations",
                                                        jab)
                                )
                                .build()
                )
                .type(MediaType.APPLICATION_JSON).build();
    }

    private static void aggregateViolations(
            JsonArrayBuilder jsonArrayBuilder,
            ConstraintViolation constraintViolation) {

        jsonArrayBuilder.add(
                Json.createObjectBuilder()
                        .add("field", constraintViolation.getPropertyPath().
                                toString())
                        .add("message", constraintViolation.getMessage())
        );

    }

}
