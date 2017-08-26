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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("PaymentMode")
//@Api(value = "PaymentMode", description = "Payment modes")
public class PaymentModeService extends CommonRestService<PaymentMode, String> {

    @Inject
    PaymentModeFacade facade;

    @Inject
    Logger logger;

    public PaymentModeService() {
        super("PaymentMode");
    }

    @Override
    protected PaymentModeFacade getFacade() {
        return facade;
    }

    @Override
    protected Logger getLogger() {
        return logger;
    }

    @Path("{name}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentMode(@PathParam("name") String name) {
        logger.log(Level.INFO, "REST request to get PaymentMode : {0}", name);
        return super.find(name);
    }

}
