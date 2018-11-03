/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.business;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.sessions.PaymentModeFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("PaymentMode")
@RolesAllowed({"Administrator"})
public class PaymentModeService extends AbstractCommonService<PaymentMode, String> {

    @Inject
    PaymentModeFacade paymentModeFacade;

    private final Logger LOG = Logger.getLogger(PaymentModeService.class.
            getName());

    public PaymentModeService() {
        super(PaymentMode.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<PaymentMode, String> getFacade() {
        return paymentModeFacade;
    }

    @RolesAllowed({"Administrator", "Professional"})
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

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response find(@PathParam("name") String name,
            @QueryParam("refresh") boolean refresh) {
        LOG.
                log(Level.INFO, "REST request to get PaymentMode : {0}",
                        name);
        return super.find(name, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(PaymentMode entity) {
        LOG.log(Level.INFO, "Creating PaymentMode {0}", super.stringify(
                entity));

        return super.post(entity, e -> {
        });
    }

    @Path("{name}")
    @DELETE
    public Response delete(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Deleting PaymentMode {0}", name);
        return super.delete(name, e -> {
        });
    }

}
