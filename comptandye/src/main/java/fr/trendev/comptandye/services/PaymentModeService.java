/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.sessions.PaymentModeFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("PaymentMode")
public class PaymentModeService extends AbstractCommonService<PaymentMode, String> {

    @Inject
    PaymentModeFacade paymentModeFacade;

    private static final Logger LOG = Logger.getLogger(PaymentModeService.class.
            getName());

    public PaymentModeService() {
        super(PaymentMode.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the PaymentMode list");
        return super.
                findAll(paymentModeFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(paymentModeFacade);
    }

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("name") String name,
            @QueryParam("refresh") boolean refresh) {
        LOG.
                log(Level.INFO, "REST request to get PaymentMode : {0}",
                        name);
        return super.find(paymentModeFacade, name, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(PaymentMode entity) {
        LOG.log(Level.INFO, "Creating PaymentMode {0}", super.stringify(
                entity));

        return super.post(entity, paymentModeFacade, e -> {
        });
    }

    @Path("{name}")
    @DELETE
    public Response delete(@PathParam("name") String name) {
        LOG.log(Level.INFO, "Deleting PaymentMode {0}", name);
        return super.delete(paymentModeFacade, name, e -> {
        });
    }

}
