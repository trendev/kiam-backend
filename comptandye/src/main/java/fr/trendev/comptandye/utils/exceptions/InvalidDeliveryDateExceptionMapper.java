/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidDeliveryDateExceptionMapper implements
        ExceptionMapper<InvalidDeliveryDateException> {

    @Override
    public Response toResponse(InvalidDeliveryDateException ex) {

        try {
            long timestamp = Long.valueOf(ex.getMessage());
            JsonObject msg = Json.createObjectBuilder().add("deliveryDate",
                    timestamp).build();

            return Response.status(Response.Status.CONFLICT).entity(Json.
                    createObjectBuilder().add("error", msg).build()).
                    build();
        } catch (NumberFormatException nfe) {
            return Response.serverError().build();
        }

    }

}
