/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe.boundaries;

import com.stripe.model.Customer;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.Subscription;
import fr.trendev.comptandye.exceptions.ThrowingFunction;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.security.controllers.AuthenticationHelper;
import fr.trendev.comptandye.stripe.controllers.StripeCustomerController;
import fr.trendev.comptandye.stripe.controllers.StripeSubscriptionController;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
@Path("stripe-subscription")
@RolesAllowed({"Administrator", "Professional"})
public class StripeSubscriptionService {

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    private AuthenticationHelper authenticationHelper;

    @Inject
    private StripeCustomerController stripeCustomerUtils;

    @Inject
    private StripeSubscriptionController stripeSubscriptionUtils;

    private final Logger LOG = Logger.getLogger(StripeSubscriptionService.class.
            getName());

    /**
     * Creates a Stripe Customer, creates a Stripe Subscription and links it
     * with the Stripe Customer
     *
     * @param sec the Security Context
     * @param payload the Stripe Source provided as JSON object
     * @param email the email of the Professional if the service is called by an
     * Administrator
     * @return the Stripe Subscription if successful, an Error otherwise
     */
    @Path("std-subscription")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscription(@Context SecurityContext sec,
            JsonObject payload,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationHelper.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            String token = payload.getString("id");
            Customer customer = this.stripeCustomerUtils.create(token, pro);

            LOG.log(Level.INFO, "Stripe Customer {0} created", customer.getId());

            Subscription subscription = this.stripeSubscriptionUtils
                    .createBasicSubscription(customer, pro);

            LOG.log(Level.INFO,
                    "Stripe Subscription {0} created and linked with Stripe Customer {1}",
                    new Object[]{subscription.getId(),
                        customer.getId()});

            pro.setStripeCustomerId(customer.getId());
            pro.setStripeSubscriptionId(subscription.getId());
            pro.setTos(true);// validation of the Terms of Services

            return Response.ok(subscription.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error creating a standard Subscription", ex);
        }
    }

    /**
     * Provides the details of a Stripe Customer
     *
     * @param sec the Security Context
     * @param email the email of the Professional if the service is called by an
     * Administrator
     * @return the Stripe Customer
     */
    @Path("details")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response details(@Context SecurityContext sec,
            @QueryParam("email") String email) {
        try {
            String proEmail = authenticationHelper.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);
            Customer customer = this.stripeCustomerUtils.details(pro);
            LOG.log(Level.INFO, "Providing details of Stripe Customer {0}/{1}",
                    new Object[]{customer.getId(),
                        proEmail});
            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error providing Customer's details on", ex);
        }
    }

    @Path("invoices")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response invoices(@Context SecurityContext sec,
            @QueryParam("email") String email) {
        try {
            String proEmail = authenticationHelper.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);
            InvoiceCollection invoices = this.stripeCustomerUtils
                    .getInvoices(pro);

            int size = invoices.getData().size();

            LOG.log(Level.INFO,
                    "Providing invoices of Stripe Customer {0}/{1}: {2}",
                    new Object[]{
                        pro.getStripeCustomerId(),
                        pro.getEmail(),
                        size});
            return Response.ok(invoices.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error providing Invoices", ex);
        }
    }

    @Path("rescind")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response rescind(@Context SecurityContext sec,
            @QueryParam("email") String email) {
        return this.manageRescission(sec, email,
                this.stripeSubscriptionUtils::rescind,
                true);
    }

    @Path("reactivate")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response reactivate(@Context SecurityContext sec,
            @QueryParam("email") String email) {
        return this.manageRescission(sec, email,
                this.stripeSubscriptionUtils::reactivate,
                false);
    }

    private Response manageRescission(SecurityContext sec,
            String email,
            ThrowingFunction<Professional, Subscription> fn,
            boolean rescind) {
        try {
            String proEmail = authenticationHelper.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);
            Subscription subscription = fn.apply(pro);

            if (rescind) {
                LOG.log(Level.INFO,
                        "Stripe Subscription {0} of the user {1} has been rescinded on {2}",
                        new Object[]{pro.
                                    getStripeSubscriptionId(), pro.getEmail(),
                            new Date(subscription.getCanceledAt() * 1000)});
            } else {
                LOG.log(Level.INFO,
                        "Stripe Subscription {0} of the user {1} has been reactivated !",
                        new Object[]{pro.
                                    getStripeSubscriptionId(), pro.
                                    getEmail()});
            }

            return Response.ok(subscription.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error " + (rescind ? "rescinding" : "reactivating")
                    + " a stripe subscription", ex);
        }
    }

}
