/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.business;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.entities.Notification;
import fr.trendev.comptandye.entities.NotificationPK;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.sessions.NotificationFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.exceptions.ExceptionHelper;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.persistence.RollbackException;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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
@Path("Notification")
@RolesAllowed({"Administrator"})
public class NotificationService extends AbstractCommonService<Notification, NotificationPK> {

    @Inject
    private NotificationFacade notificationFacade;

    @Inject
    private ProfessionalFacade professionalFacade;

    private final Logger LOG = Logger.getLogger(NotificationService.class.
            getName());

    public NotificationService() {
        super(Notification.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Notification, NotificationPK> getFacade() {
        return notificationFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
    }

    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        NotificationPK pk = new NotificationPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Notification : {0}",
                notificationFacade.
                        prettyPrintPK(
                                pk));
        return super.find(pk, refresh);
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response post(@Context SecurityContext sec, Notification entity,
//            @QueryParam("professional") String professional) {
//
//        String email = this.getProEmail(sec, professional);
//
//        return super.<Professional, String>post(entity, email,
//                AbstractFacade::prettyPrintPK,
//                Professional.class,
//                professionalFacade,
//                Notification::setProfessional,
//                Professional::getNotifications, e -> {
//            e.setId(null);
//        });
//
//    }
//    @RolesAllowed({"Administrator", "Professional"})
//    @Path("check")
//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response put(@Context SecurityContext sec, Notification entity,
//            @QueryParam("professional") String professional) {
//
//        NotificationPK pk = new NotificationPK(entity.getId(), this.
//                getProEmail(sec,
//                        professional));
//
//        LOG.log(Level.INFO, "Updating Notification {0}",
//                notificationFacade.
//                        prettyPrintPK(pk));
//        return super.put(entity, pk, e -> {
//            if (!e.isChecked()) {
//                e.setChecked(true);
//            }// else do nothing
//        });
//    }
    @RolesAllowed({"Administrator", "Professional"})
    @Path("check/{id}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        NotificationPK pk = new NotificationPK(id, this.
                getProEmail(sec,
                        professional));

        LOG.log(Level.INFO, "Checking Notification {0}",
                notificationFacade.
                        prettyPrintPK(pk));
        try {
            return Optional.ofNullable(notificationFacade.find(pk))
                    .map(result -> {
                        if (!result.isChecked()) {
                            result.setChecked(true);
                            LOG.log(Level.INFO,
                                    "Notification {0} checked",
                                    notificationFacade.prettyPrintPK(pk));
                        }// else do nothing
                        return Response.status(Response.Status.OK).entity(
                                result).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Notification "
                                    + notificationFacade.prettyPrintPK(pk)
                                    + " not found").
                                    build()).
                            build());
        } catch (EJBTransactionRolledbackException | RollbackException ex) {
            throw ex;
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs updating Notification "
                    + notificationFacade.prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    @RolesAllowed({"Administrator", "Professional"})
    @Path("check-all")
    @PUT
    public Response checkAll(@Context SecurityContext sec,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);

        try {
            return Optional.ofNullable(professionalFacade.find(proEmail))
                    .map(pro -> {
                        LOG.log(Level.INFO,
                                "Checking All Notifications of Professional Account {0}",
                                proEmail);
                        int checked = notificationFacade.checkAll(pro);
                        return Response.ok(Json.createObjectBuilder()
                                .add("totalChecked", checked).build()
                        ).build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Professional"
                                    + proEmail
                                    + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {
            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs checking All Notifications of Professional Account"
                    + proEmail);
            LOG.log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }

    }

    @RolesAllowed({"Administrator", "Professional"})
    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        NotificationPK pk = new NotificationPK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Notification {0}",
                notificationFacade.
                        prettyPrintPK(pk));

        try {
            return Optional.ofNullable(notificationFacade.find(pk))
                    .map(result -> {
                        result.getProfessional().getNotifications().remove(
                                result);
                        return Response.ok().build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Notification "
                                    + notificationFacade.prettyPrintPK(pk)
                                    + " not found").
                                    build()).
                            build());
        } catch (EJBTransactionRolledbackException | RollbackException ex) {
            throw ex;
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs deleting Notification "
                    + notificationFacade.prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }

    }

    @RolesAllowed({"Administrator", "Professional"})
    @Path("delete-all")
    @DELETE
    public Response deleteAll(@Context SecurityContext sec,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);

        try {
            return Optional.ofNullable(professionalFacade.find(proEmail))
                    .map(pro -> {
                        LOG.log(Level.INFO,
                                "Deleting All Notifications of Professional Account {0}",
                                proEmail);
                        // using Orphan Removal attribute on Professional.notifications 
                        pro.setNotifications(new ArrayList<Notification>());
                        return Response.ok().build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Professional"
                                    + proEmail
                                    + " not found").
                                    build()).
                            build());
        } catch (Exception ex) {
            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs deleting All Notifications of Professional Account"
                    + proEmail);
            LOG.log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }

    }

}
