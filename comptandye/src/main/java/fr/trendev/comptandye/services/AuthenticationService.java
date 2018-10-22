/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.beans.xsrf.XSRFTokenGenerator;
import fr.trendev.comptandye.sessions.UserAccountFacade;
import fr.trendev.comptandye.utils.ActiveSessionTracker;
import fr.trendev.comptandye.utils.AuthenticationSecurityUtils;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.exceptions.ExceptionHandler;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.io.StringReader;
import java.security.Principal;
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
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
@PermitAll
public class AuthenticationService {

    @Inject
    PasswordGenerator passwordGenerator;

    @Inject
    UserAccountFacade userAccountFacade;

    @Inject
    ActiveSessionTracker tracker;

    @Inject
    XSRFTokenGenerator generator;

    @Inject
    AuthenticationSecurityUtils securityUtils;

    @Inject
    ExceptionHandler exceptionHandler;

    private final Logger LOG = Logger.getLogger(AuthenticationService.class.
            getName());

    @Path("password")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String password(@QueryParam("size") int size) {
        return passwordGenerator.autoGenerate(size);
    }

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
        return securityUtils.getProfessionalEmailFromSecurityContext(sec)
                .map(email -> {
                    LOG.log(Level.INFO, "Providing the profile of [{0}]", email);
                    return Response.ok(userAccountFacade.find(email)).build();
                })
                .orElse(
                        Response.status(Response.Status.UNAUTHORIZED)
                                .entity(Json.createObjectBuilder().add("error",
                                        "Unauthorized or Blocked User").build())
                                .build()
                );
    }

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
            //user is not authenticated
            if (user == null) {

                /**
                 * Get a session and Validate the provided username and password
                 * in the password validation realm used by the web container
                 * login mechanism configured for the ServletContext.
                 */
                HttpSession session = req.getSession();
                req.login(username, password);

                // checks first if the user is Blocked or not
                if (securityUtils.isBlockedUser(sec)) {
                    LOG.log(Level.WARNING,
                            "Login cancelled - user [{0}] is Blocked",
                            username);
                    try {
                        session.invalidate();
                    } catch (IllegalStateException ex) {
                    }

                    return Response.status(Response.Status.UNAUTHORIZED)
                            .entity(Json.createObjectBuilder().add("error",
                                    "Login FAILED - user [" + username
                                    + "] is Blocked").
                                    build())
                            .build();
                } else {

                    //adds the session of the new authenticated user in the tracker
                    tracker.put(username, session);

                    SessionCookieConfig scc = req.
                            getServletContext().getSessionCookieConfig();

                    NewCookie jsessionid = new NewCookie("JSESSIONID",
                            session.getId(),
                            scc.getPath(),
                            scc.getDomain(),
                            null,
                            scc.getMaxAge(), true, true);

                    String token = generator.generate();
                    session.setAttribute("XSRF-TOKEN", token);
                    NewCookie xsrfCookie = new NewCookie("XSRF-TOKEN",
                            token,
                            scc.getPath(),
                            scc.getDomain(),
                            null,
                            scc.getMaxAge(), true, false);

                    //sends the profile and set the cookies
                    return Response.fromResponse(this.profile(sec))
                            .cookie(jsessionid, xsrfCookie)
                            .build();
                }
            } else {// user is already authenticated
                LOG.log(Level.WARNING,
                        "Login process cancelled - user [{0}] is already logged in",
                        user.getName());

            }
            //just sends the profile if the user is authenticated
            return this.profile(sec);
        } catch (ServletException ex) {
            LOG.log(Level.WARNING, ExceptionHelper.handleException(ex,
                    "Login FAILED - user [" + username + "] / [" + req.
                            getRemoteAddr() + "]"));
        }
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(Json.createObjectBuilder().add("error",
                        "Login FAILED - user [" + username + "]").build())
                .build();
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
    @RolesAllowed({"Administrator", "Professional", "Individual"})
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

    @RolesAllowed({"Administrator", "Professional", "Individual"})
    @Path("new-password")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newPassword(@Context SecurityContext sec, String newPassword) {

        String password = this.readNewPassword(newPassword);

        return securityUtils.getProfessionalEmailFromSecurityContext(sec)
                .map(email ->
                        Optional.ofNullable(this.userAccountFacade.find(email))
                                .map(user -> {
                                    String epwd = passwordGenerator.
                                            encrypt_SHA256(password);
                                    user.setPassword(epwd);
                                    String msg = "Password of user ["
                                            + email
                                            + "] has been changed !";
                                    LOG.log(Level.INFO, msg);
                                    return Response.ok(
                                            Json.createObjectBuilder().
                                                    add("password", epwd).
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

        String password = null;

        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            password = reader.readObject().getString("newpassword");

            if (password.isEmpty() || password == null) {
                String errmsg = "The new password must not be null or empty";
                throw new IllegalArgumentException(errmsg);
            }
            return password;
        } catch (Exception ex) {
            String errmsg = "Impossible to read the new password from json object "
                    + json;
            throw new WebApplicationException(
                    "Impossible to read the new password from json object "
                    + json + " : " + ex.getMessage());
        }
    }
}
