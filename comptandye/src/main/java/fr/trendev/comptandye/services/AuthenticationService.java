/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
import fr.trendev.comptandye.sessions.UserAccountFacade;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UserAccountType;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.security.Principal;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @Inject
    ActiveSessionTracker tracker;

    private final Logger LOG = Logger.getLogger(AuthenticationService.class.
            getName());

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
                .map(email -> {
                    LOG.log(Level.INFO, "Providing the profile of [{0}]", email);
                    return Response.ok(userAccountFacade.find(email)).build();
                })
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

    @PermitAll
    @Path("login")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @Context HttpServletRequest req,
            @Context SecurityContext sec,
            @QueryParam("username") String username,
            @QueryParam("password") String password) {

        Principal user = req.getUserPrincipal();

        try {
            if (user == null) {
                req.login(username, password);
                HttpSession session = req.getSession();
            } else {// user is authenticated
                LOG.log(Level.WARNING,
                        "Login Failed - [{0}] is already logged in",
                        user.getName());
            }
            return this.profile(sec);
        } catch (ServletException ex) {
            LOG.log(Level.SEVERE, ExceptionHelper.handleException(ex,
                    "Login FAILED - [" + username + "] / [" + req.
                            getRemoteAddr() + "]"));
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Json.createObjectBuilder().add("error",
                        "Login FAILED - [" + username + "]").build())
                .build();
    }

    @Path("logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context SecurityContext sec) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
    }
}
