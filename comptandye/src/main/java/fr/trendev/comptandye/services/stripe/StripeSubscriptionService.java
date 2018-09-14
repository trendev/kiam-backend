/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.stripe;

import com.stripe.model.Customer;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.Subscription;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.AuthenticationSecurityUtils;
import fr.trendev.comptandye.utils.stripe.StripeCustomerUtils;
import fr.trendev.comptandye.utils.stripe.StripeSubscriptionUtils;
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
@Path("StripeSubscription")
@RolesAllowed({"Administrator", "Professional"})
public class StripeSubscriptionService {

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    private AuthenticationSecurityUtils authenticationSecurityUtils;

    @Inject
    private StripeCustomerUtils stripeCustomerUtils;

    @Inject
    private StripeSubscriptionUtils stripeSubscriptionUtils;

    private final Logger LOG = Logger.getLogger(StripeSubscriptionService.class.
            getName());

    /**
     * Creates a Stripe Customer, creates a Stripe Subscription and links it
     * with the Stripe Customer
     *
     * @param sec the Security Context
     * @param stripeSourceJson the Stripe Source provided as JSON object
     * @param email the email of the Professional if the service is called by an
     * Administrator
     * @return the Stripe Subscription if successful, an Error otherwise
     */
    @Path("std-subscription")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscription(@Context SecurityContext sec,
            JsonObject stripeSourceJson,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationSecurityUtils.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            String sourceId = stripeSourceJson.getString("id");
            Customer customer = this.stripeCustomerUtils.create(sourceId, pro);

            LOG.log(Level.INFO, "Stripe Customer {0} created", customer.getId());

            Subscription subscription = this.stripeSubscriptionUtils
                    .createDefaultSubscription(customer, pro);

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
            String proEmail = authenticationSecurityUtils.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);
            Customer customer = this.stripeCustomerUtils.details(pro);
            LOG.log(Level.INFO, "Providing details of Stripe Customer {0}",
                    customer.getId());
            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error providing Customer's details on", ex);
        }
    }

    /**
     * Adds a Stripe Source to an exising Stripe Customer
     *
     * @param sec the Security Context
     * @param stripeSourceJson the new Stripe Source
     * @param email the email of the Professional if the service is called by an
     * Administrator
     * @return the Stripe Customer (should contain the new Stripe Source in its
     * sources)
     */
    @Path("add-source")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addSource(@Context SecurityContext sec,
            JsonObject stripeSourceJson,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationSecurityUtils.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            String sourceId = stripeSourceJson.getString("id");

            Customer customer = this.stripeCustomerUtils.
                    addSource(sourceId, pro);

            LOG.log(Level.INFO,
                    "Stripe Source {0} added to Stripe Customer {1}/{2}",
                    new Object[]{sourceId,
                        customer.getId(), proEmail});

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error adding a Source to an existing Customer", ex);
        }
    }

    /**
     * Sets a Stripe Source as the default Stripe Source which will be used for
     * the future Subscriptions/Charges
     *
     * @param sec the Security Context
     * @param sourceId the id of the Stripe Source to set as default source
     * @param email the email of the Professional if the service is called by an
     * Administrator
     * @return the Stripe Customer (should contain the updated sources list)
     */
    @Path("default_source/{default_source}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response defaultSource(@Context SecurityContext sec,
            @PathParam("default_source") String sourceId,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationSecurityUtils.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            Customer customer = this.stripeCustomerUtils.
                    defaultSource(sourceId, pro);

            LOG.log(Level.INFO,
                    "Stripe Source {0} is the new default source of Stripe Customer {1}/{2}",
                    new Object[]{sourceId,
                        customer.getId(), proEmail});

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error setting the default Source of an existing Customer",
                    ex);
        }
    }

    /**
     * Detaches (removes) a Stripe Source from a Stripe Customer
     *
     * @param sec the Security Context
     * @param sourceId the id of the Stripe Source to detach(remove)
     * @param email the email of the Professional if the service is called by an
     * Administrator
     * @return the Stripe Customer (should contain the updated sources list)
     */
    @Path("detach/{source}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response detachSource(@Context SecurityContext sec,
            @PathParam("source") String sourceId,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationSecurityUtils.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            Customer customer = this.stripeCustomerUtils.
                    detachSource(sourceId, pro);

            LOG.log(Level.INFO,
                    "Stripe Source {0} is now detached from Stripe Customer {1}/{2}",
                    new Object[]{sourceId,
                        customer.getId(), proEmail});

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error detaching a Source of an existing Customer",
                    ex);
        }
    }

    @Path("invoices")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response invoices(@Context SecurityContext sec,
            @QueryParam("email") String email) {
        try {
            String proEmail = authenticationSecurityUtils.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);
            InvoiceCollection invoices = this.stripeCustomerUtils
                    .getInvoices(pro);
            LOG.log(Level.INFO,
                    "Providing invoices of Stripe Customer {0}/{1}: {2} invoices",
                    new Object[]{pro.getStripeCustomerId(), pro.getEmail(),
                        invoices.getTotalCount()});
            return Response.ok(invoices.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error providing Invoices", ex);
        }
    }

}
