/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
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
        List<ValidationError> errors = ((ConstraintViolationException) exception).
                getConstraintViolations().
                stream()
                .map(ExceptionHelper::toValidationError)
                .collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST).entity(
                errors)
                .type(MediaType.APPLICATION_JSON).build();
    }

    private static ValidationError toValidationError(
            ConstraintViolation constraintViolation) {
        ValidationError error = new ValidationError();
        error.setField(constraintViolation.getPropertyPath().toString());
        error.setMessage(constraintViolation.getMessage());
        return error;

    }

    private static class ValidationError {

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String field;
        private String message;

    }
}
