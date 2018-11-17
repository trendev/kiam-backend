/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.boundaries;

import fr.trendev.comptandye.exceptions.ExceptionHandler;
import fr.trendev.comptandye.security.controllers.AuthenticationHelper;
import fr.trendev.comptandye.security.controllers.PasswordManager;
import fr.trendev.comptandye.security.controllers.XSRFTokenGenerator;
import fr.trendev.comptandye.useraccount.controllers.UserAccountFacade;
import java.io.StringReader;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonReader;
import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
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
    PasswordManager passwordManager;

    @Inject
    UserAccountFacade userAccountFacade;

    @Inject
    XSRFTokenGenerator generator;

    @Inject
    AuthenticationHelper authenticationHelper;

    @Inject
    ExceptionHandler exceptionHandler;

    private final Logger LOG = Logger.getLogger(AuthenticationService.class.
            getName());

    @PermitAll
    @Path("password")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String password(@QueryParam("size") int size) {
        return passwordManager.autoGenerate(size);
    }

//    @PermitAll
    @Path("profile")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void profile(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec) {
        CompletableFuture
                .supplyAsync(() -> this.profile(sec))
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));
    }

    private Response profile(SecurityContext sec) {
        return authenticationHelper.getProfessionalEmailFromSecurityContext(sec)
                .map(email -> {
                    LOG.log(Level.INFO, "Providing the profile of [{0}]", email);
                    return Response.ok(userAccountFacade.find(email)).build();
                })
                // should never happen (method used by secured methods
                .orElseThrow(() -> new NotAuthorizedException(Response.status(
                        Response.Status.UNAUTHORIZED).build()));

    }

    @Path("login")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @Context HttpServletRequest req,
            @Context SecurityContext sec,
            @QueryParam("username") String username,
            @QueryParam("password") String password) {
        return this.profile(sec);
    }

    /**
     * Logs out the user, invalidating the current session and setting the
     * cookies with null value and 0 timeout.
     *
     * @param req the origin request
     * @param sec the security context
     * @return 200 if everything is OK or 417 Expectation failed if the session
     * is invalidated
     */
    @Path("logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(
            @Context HttpServletRequest req,
            @Context SecurityContext sec) {

        String username = sec.getUserPrincipal().getName();
        SessionCookieConfig scc = req.
                getServletContext().getSessionCookieConfig();

        NewCookie jsessionid = new NewCookie("JSESSIONID",
                null,
                scc.getPath(),
                scc.getDomain(),
                null,
                0, true, true);

        NewCookie xsrfCookie = new NewCookie("XSRF-TOKEN",
                null,
                scc.getPath(),
                scc.getDomain(),
                null,
                0, true, false);

        try {
            //Get the current session and invalidate it
            Optional.ofNullable(req.getSession(false))
                    .ifPresent(session -> session.invalidate());
        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder()
                            .add("msg", "user " + username
                                    + " is now logged out").
                            build()
            )
                    .cookie(jsessionid, xsrfCookie)
                    .build();
        }

        return Response.ok(
                Json.createObjectBuilder()
                        .add("msg", "user " + username + " is now logged out").
                        build()
        )
                .cookie(jsessionid, xsrfCookie)
                .build();
    }

    @Path("new-password")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newPassword(@Context SecurityContext sec, String newPassword) {

        String password = this.readNewPassword(newPassword);

        return authenticationHelper.getProfessionalEmailFromSecurityContext(sec)
                .map(email ->
                        Optional.ofNullable(this.userAccountFacade.find(email))
                                .map(user -> {
                                    String hpwd = passwordManager.hashPassword(
                                            password);
                                    user.setPassword(hpwd);
                                    String msg = "Password of user ["
                                            + email
                                            + "] has been changed !";
                                    LOG.log(Level.INFO, msg);
                                    return Response.ok(
                                            Json.createObjectBuilder().
                                                    add("password", hpwd).
                                                    build()
                                    ).build();
                                })
                                .orElseThrow(() ->
                                        new WebApplicationException(
                                                "the user [" + email
                                                + "] cannot be found"))
                ).orElseThrow(() -> new WebApplicationException(
                        "email of an authenticated user should not be null"));
    }

    private String readNewPassword(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            return Optional.of(reader.readObject().getString("newpassword"))
                    .filter(pwd -> !pwd.isEmpty())
                    .get();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Impossible to read the new password from json object "
                    + json + " : " + ex.getMessage());
        }
    }
}
