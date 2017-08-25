/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.exceptions;

import javax.json.Json;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

//Uncomment in order to provide this ExceptionMapper
//@Provider
public class DebugExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception ex) {
        ex.printStackTrace();
        String errmsg = ExceptionHelper.handleException(ex,
                "DebugExceptionMapper caught an Exception");
        return Response.serverError().entity(Json.createObjectBuilder().add(
                "error", errmsg).build()).build();
    }
}
