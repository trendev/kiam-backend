/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.sessions.PaymentFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("Payment")
public class PaymentService extends AbstractCommonService<Payment, Long> {

    @Inject
    PaymentFacade paymentFacade;

    private static final Logger LOG = Logger.getLogger(PaymentService.class.
            getName());

    public PaymentService() {
        super(Payment.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Payment list");
        return super.findAll(paymentFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(paymentFacade);
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get Payment : {0}", id);
        return super.find(paymentFacade, id, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(Payment entity) {
        LOG.log(Level.INFO, "Creating Payment {0}", super.stringify(entity));

        return super.post(entity, paymentFacade, e -> {
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(Payment entity) {
        LOG.log(Level.INFO, "Updating Payment {0}", entity.getId());
        return super.put(entity, paymentFacade, entity.getId(),
                e -> {
            e.setAmount(entity.getAmount());
            e.setCurrency(entity.getCurrency());
            //TODO : avoid to create a new PaymentMode if the provided PaymentMode does not exist
            e.setPaymentMode(entity.getPaymentMode());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting Payment {0}", id);
        return super.delete(paymentFacade, id, e -> {
        });
    }
}
