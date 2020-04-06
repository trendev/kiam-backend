/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe.boundaries;

import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.security.controllers.AuthenticationHelper;
import fr.trendev.comptandye.stripe.controllers.StripeCustomerController;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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

    @Inject
    private StripeCustomerController stripeCustomerCtrl;

    private static final Logger LOG = Logger.getLogger(
            StripePaymentMethodService.class.getName());

    /**
     * Get the Stripe payment methods of a Professional user
     * @param sec the security context  
     * @param email optional user's email
     * @param t the payment type (card)
     * @return a HTTP response
     */
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

    @Path("add/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPaymentMethod(@Context SecurityContext sec,
            @PathParam("id") String id,
            @QueryParam("email") String email) {
        try {
            String proEmail = authenticationHelper.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            Customer customer = this.stripeCustomerCtrl.
                    addPaymentMethod(id, pro);

            LOG.log(Level.INFO,
                    "Stripe PaymentMethod {0} added to Stripe Customer {1}/{2}",
                    new Object[]{id,
                        customer.getId(), proEmail});

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error adding a PaymentMethod to an existing Customer", ex);
        }
    }

    @Path("detach/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response detachPaymentMethod(@Context SecurityContext sec,
            @PathParam("id") String id,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationHelper.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            Customer customer = this.stripeCustomerCtrl.
                    detachPaymentMethod(id, pro);

            LOG.log(Level.INFO,
                    "Stripe PaymentMethod {0} is now detached from user {1}/{2}",
                    new Object[]{id,
                        customer.getId(),
                        proEmail});

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error detaching a PaymentMethod of an existing Customer",
                    ex);
        }
    }

    @Path("default/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response defaultPaymentMethod(@Context SecurityContext sec,
            @PathParam("id") String id,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationHelper.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            Customer customer = this.stripeCustomerCtrl.
                    defaultPaymentMethod(id, pro);

            LOG.log(Level.INFO,
                    "Stripe PaymentMethod {0} is the new default source of Stripe Customer {1}/{2}",
                    new Object[]{id,
                        customer.getId(), proEmail});

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error setting the default PaymentMethod of an existing Customer",
                    ex);
        }
    }

}
