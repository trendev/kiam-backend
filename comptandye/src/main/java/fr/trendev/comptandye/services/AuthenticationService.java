/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.sessions.UserAccountFacade;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UserAccountType;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
    UserAccountFacade userAccountFacade;

    @Path("password")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String password(@QueryParam("size") int id) {
        return PasswordGenerator.autoGenerate(id);
    }

    @Path("profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response profile(@Context SecurityContext sec) {
        return this.getEmail(sec)
                .map(email -> Response.ok(userAccountFacade.find(email)).
                        build())
                .orElse(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity(Json.createObjectBuilder().add("error",
                                        "Unauthorized User").build())
                                .build()
                );
    }

    private Optional<String> getEmail(SecurityContext sec) {
        return Optional.ofNullable(
                sec.isSecure() && (sec.
                isUserInRole(UserAccountType.PROFESSIONAL)
                || sec.isUserInRole(UserAccountType.INDIVIDUAL)
                || sec.isUserInRole(UserAccountType.ADMINISTRATOR))
                ? sec.getUserPrincipal().getName() : null);
    }

    @Path("logout")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context SecurityContext sec) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}
