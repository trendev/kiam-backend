/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe.boundaries;

import com.stripe.exception.StripeException;
import com.stripe.model.SetupIntent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("setup-intent")
@RolesAllowed({"Administrator", "Professional"})
public class StripeSetupIntentService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(
            @QueryParam("payment_method_types") String payment_method_types) {
        try {
            Map<String, Object> setupIntentParams = new HashMap<>();

            if (payment_method_types != null
                    && !payment_method_types.isEmpty()) {
                ArrayList<String> paymentMethodTypes = new ArrayList<>();
                paymentMethodTypes.add(payment_method_types);
                setupIntentParams.
                        put("payment_method_types", paymentMethodTypes);
            }

            SetupIntent si = SetupIntent.create(setupIntentParams);

            return Response.ok(si.toJson()).build();
        } catch (StripeException ex) {
            throw new WebApplicationException(
                    "Error creating a Stripe SetupIntent", ex);
        }
    }

    @Path("cancel/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator"})
    public Response cancel(
            @PathParam("id") String id) {
        try {
            SetupIntent intent = SetupIntent.retrieve(id);
            Map<String, Object> setupIntentParams = new HashMap<>();
            setupIntentParams.put("cancellation_reason", "abandoned");
            SetupIntent canceledSetupIntent = intent.cancel(setupIntentParams);
            return Response.ok(canceledSetupIntent.toJson()).build();
        } catch (StripeException ex) {
            throw new WebApplicationException(
                    "Error canceling a Stripe SetupIntent", ex);
        }
    }
}
