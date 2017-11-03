/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
import fr.trendev.comptandye.beans.xsrf.XSRFTokenGenerator;
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
import javax.servlet.SessionCookieConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
    UserAccountFacade userAccountFacade;

    @Inject
    ActiveSessionTracker tracker;

    @Inject
    XSRFTokenGenerator generator;

    private final Logger LOG = Logger.getLogger(AuthenticationService.class.
            getName());

    @Path("password")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String password(@QueryParam("size") int size) {
        return PasswordGenerator.autoGenerate(size);
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
                                        "Unauthorized or Blocked User").build())
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

    private boolean isBlockedUser(SecurityContext sec) {
        return this.getEmail(sec)
                .map(u -> false)
                .orElse(true);
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
                req.login(username, password);
                HttpSession session = req.getSession();

                // checks first if the user is Blocked or not
                if (this.isBlockedUser(sec)) {
                    LOG.log(Level.WARNING,
                            "Login cancelled - user [{0}] is Blocked",
                            username);
                    this.logout(req, sec);
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

                    NewCookie xsrfCookie = new NewCookie("XSRF-TOKEN",
                            generator.generate(session),
                            scc.getPath(),
                            scc.getDomain(),
                            null,
                            scc.getMaxAge(), true, false);

                    //sends the profile and set the cookies
                    return Response.fromResponse(this.profile(sec))
                            .cookie(jsessionid, xsrfCookie)
                            .build();
                }
            } else {// user is authenticated
                LOG.log(Level.WARNING,
                        "Login cancelled - user [{0}] is already logged in",
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
            req.getSession().invalidate();
        } catch (IllegalStateException ex) {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    Json.createObjectBuilder()
                            .add("msg", "user " + username + " logged out").
                            build()
            )
                    .cookie(jsessionid, xsrfCookie)
                    .build();
        }

        return Response.ok(
                Json.createObjectBuilder()
                        .add("msg", "user " + username + " logged out").build()
        )
                .cookie(jsessionid, xsrfCookie)
                .build();
    }
}
