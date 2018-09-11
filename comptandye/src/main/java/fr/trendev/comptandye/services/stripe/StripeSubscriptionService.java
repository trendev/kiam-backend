/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.stripe;

import com.stripe.model.Customer;
import com.stripe.model.Source;
import com.stripe.model.Subscription;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.AuthenticationSecurityUtils;
import fr.trendev.comptandye.utils.stripe.StripeCustomerUtils;
import fr.trendev.comptandye.utils.stripe.StripeSubscriptionUtils;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

    @Path("std-subscription")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscription(@Context SecurityContext sec,
            JsonObject stripeSourceJson,
            @QueryParam("email") String email) {
        try {

            String proEmail = authenticationSecurityUtils.
                    getProEmail(sec, email);
            Professional pro = professionalFacade.find(proEmail);

            // TODO : use the source id instead of retrieving it
            Source source = Source.retrieve(stripeSourceJson.getString("id"));

            Customer customer = this.stripeCustomerUtils.create(source, pro);

            Subscription subscription = this.stripeSubscriptionUtils
                    .createDefaultSubscription(customer, pro);

            pro.setStripeCustomerId(customer.getId());
            pro.setStripeSubscriptionId(subscription.getId());
            pro.setTos(true);

            return Response.ok(subscription.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error creating a standard Subscription", ex);
        }
    }

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
            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error getting details on a subscription", ex);
        }
    }

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

            // TODO : use the source id instead of retrieving it
            Source source = Source.retrieve(stripeSourceJson.getString("id"));

            Customer customer = this.stripeCustomerUtils.addSource(source, pro);

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error creating a standard Subscription", ex);
        }
    }

}
