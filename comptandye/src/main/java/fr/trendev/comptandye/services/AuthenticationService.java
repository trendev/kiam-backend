/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.utils.PasswordGenerator;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Authentication")
@RolesAllowed({"Administrator", "Professional", "Individual"})
public class AuthenticationService {

    @Inject
    ProfessionalService professionalService;

    @Inject
    IndividualService individualService;

    @Inject
    AdministratorService administratorService;

    @Path("password")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String find(@QueryParam("size") int id) {
        return PasswordGenerator.autoGenerate(id);
    }

    @Path("authorized")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorized(@Context SecurityContext sec) {
        if (sec.isSecure() && sec.isUserInRole("Professional")) {
            return professionalService.profile(sec, false);
        }

        if (sec.isSecure() && sec.isUserInRole("Individual")) {
            return individualService.profile(sec, false);
        }

        if (sec.isSecure() && sec.isUserInRole("Administrator")) {
            return administratorService.find(sec.getUserPrincipal().getName(),
                    true);
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Json.createObjectBuilder().add("error",
                        "Unauthorized User").build())
                .build();
    }
}
