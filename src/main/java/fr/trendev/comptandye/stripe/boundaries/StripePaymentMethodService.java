/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe.boundaries;

import com.stripe.model.PaymentMethod;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.security.controllers.AuthenticationHelper;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("stripe-payment-method")
@RolesAllowed({"Administrator", "Professional"})
public class StripePaymentMethodService {

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    private AuthenticationHelper authenticationHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPaymentMethods(@Context SecurityContext sec,
            @QueryParam("email") String email,
            @QueryParam("t") String t) {

        String proEmail = authenticationHelper.getProEmail(sec, email);

        try {
            Professional pro = professionalFacade.find(proEmail);

            Map<String, Object> paymentmethodParams = new HashMap<>();
            paymentmethodParams.put("customer", pro.getStripeCustomerId());
            paymentmethodParams.put("type",
                    (t != null && !t.isEmpty()) ? t : "card");

            return Response.ok(PaymentMethod.list(paymentmethodParams).toJson()).
                    build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Exception occurs returning a list of PaymentMethods for user "
                    + proEmail, ex);
        }

    }

}
