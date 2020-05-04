/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.boundaries;

import fr.trendev.kiam.exceptions.ExceptionHandler;
import fr.trendev.kiam.security.controllers.AuthenticationHelper;
import fr.trendev.kiam.security.controllers.PasswordManager;
import fr.trendev.kiam.security.controllers.jwt.JWTManager;
import fr.trendev.kiam.security.entities.NewPassword;
import fr.trendev.kiam.useraccount.controllers.UserAccountFacade;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
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
    PasswordManager passwordManager;

    @Inject
    UserAccountFacade userAccountFacade;

    @Inject
    AuthenticationHelper authHelper;

    @Inject
    ExceptionHandler exceptionHandler;

    @Inject
    JWTManager jwtm;

    private final Logger LOG = Logger.getLogger(AuthenticationService.class.
            getName());

    @PermitAll
    @Path("password")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String password(@QueryParam("size") int size) {
        return passwordManager.autoGenerate(size);
    }

    @RolesAllowed({"Administrator"})
    @Path("hash/{pwd}")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    public String hashpwd(@PathParam("pwd") String pwd) {
        return passwordManager.hashPassword(pwd);
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
        return authHelper.getUserEmailFromSecurityContext(sec)
                .map(email -> Optional.ofNullable(
                userAccountFacade.find(email))
                .map(profile -> Response.ok(profile).build())
                .orElse(Response
                        .status(Response.Status.NOT_FOUND)
                        .build())
                )
                // should never happen (method used by secured methods)
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
        LOG.log(Level.INFO, "{0} is authenticating...", username.trim());
        return this.profile(sec);
    }

    @Path("logout")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(
            @Context HttpServletRequest req,
            @Context SecurityContext sec) {

        String email = sec.getUserPrincipal().getName();

        return this.authHelper.getJWTFromRequestHeader(req)
                .flatMap(t -> jwtm.revokeToken(email, t))
                .map(r -> Response.ok(
                Json.createObjectBuilder()
                        .add("msg", "user [" + email
                                + "] is now logged out"
                                + " and token ("
                                + JWTManager.
                                        trunkToken(r.getToken())
                                + ") is now revoked").
                        build())
                .build())
                .orElseThrow(() -> new WebApplicationException(
                "INVALID JWT Header (NOT PRESENT or NOT FORMAL): "
                + "Impossible to revoke the JWT of user ["
                + email + "] during processing its logout"));

    }

    @Path("new-password")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response newPassword(@Context SecurityContext sec, NewPassword npwd) {

        String password = this.readNewPassword(npwd).trim();

        return authHelper.getUserEmailFromSecurityContext(sec)
                .map(email
                        -> Optional.ofNullable(this.userAccountFacade.find(email))
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
                        .orElseThrow(()
                                -> new WebApplicationException(
                                "the user [" + email
                                + "] cannot be found"))
                ).orElseThrow(() -> new WebApplicationException(
                "email of an authenticated user should not be null"));
    }

    private String readNewPassword(NewPassword npwd) {
        return Optional.of(npwd.getNewpassword())
                .filter(s -> !s.isEmpty())
                .orElseThrow(()
                        -> new WebApplicationException(
                        "Impossible to read the new password"));
    }

}
