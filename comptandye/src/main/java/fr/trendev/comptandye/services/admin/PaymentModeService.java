/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.admin;

import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import fr.trendev.comptandye.sessions.PaymentModeFacade;
import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("/admin/payment-mode")
public class PaymentModeService {

    @Inject
    PaymentModeFacade facade;

    private static final Logger LOG = Logger.getLogger(PaymentModeService.class.
            getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing all payment-modes");
        try {
            List<PaymentMode> list = facade.findAll();
            LOG.log(Level.INFO, "{0} payment-mode", list.size());

            return Response.status(Response.Status.OK).entity(
                    new GenericEntity<List<PaymentMode>>(
                            list) {
            }).
                    build();
        } catch (Exception ex) {
            Throwable t = ExceptionHelper.
                    findRootCauseException(ex);

            String msg = MessageFormat.format(
                    "Exception occurs providing the payment-mode list for an administrator: {0} ; {1}",
                    new Object[]{t.getClass().toString(), t.getMessage()});

            LOG.
                    log(Level.WARNING, msg);
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(
                    msg).
                    build();
        }
    }

}
