/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.AuthenticationSecurityUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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

    private final Logger LOG = Logger.getLogger(StripeSubscriptionService.class.
            getName());

    @Path("std-subscription")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response subscription(@Context SecurityContext sec,
            JsonObject stripeSourceJson) {
        try {

            String email = authenticationSecurityUtils.getProEmail(sec, null);
            Professional pro = professionalFacade.find(email);

            // should you the source id instead of retrieving it
            Source source = Source.retrieve(stripeSourceJson.getString("id"));

            Customer customer = this.createCustomer(source, pro);

            return Response.ok(customer.toJson()).build();
        } catch (Exception ex) {
            throw new WebApplicationException("Error retrieving a Stripe Source",
                    ex);
        }
    }

    private Customer createCustomer(Source source, Professional pro) throws
            StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source.getId());
        params.put("email", pro.getEmail());
        params.put("description", pro.getCltype());

        Map<String, String> customerMetadata = new HashMap<>();
        customerMetadata.put("uuid", pro.getUuid());
        customerMetadata.put("companyName", pro.getCompanyName());
        customerMetadata.put("companyID", pro.getCompanyID());
        params.put("metadata", customerMetadata);

        Map<String, Object> customerShipping = new HashMap<>();
        customerShipping.put("name", pro.getCustomerDetails().getFirstName()
                + " " + pro.getCustomerDetails().getLastName());
        // must be international format
        customerShipping.put("phone", pro.getCustomerDetails().getPhone());
        Map<String, String> customerShippingAddress = new HashMap<>();
        customerShippingAddress.put("line1", pro.getAddress().getStreet());
        customerShippingAddress.put("city", pro.getAddress().getCity());
        customerShippingAddress.
                put("country", pro.getAddress().getCountry());
        customerShippingAddress.
                put("postal_code", pro.getAddress().getPostalCode());
        customerShipping.put("address", customerShippingAddress);
        params.put("shipping", customerShipping);

        if (pro.getVatcode() != null && !pro.getVatcode().isEmpty()) {
            Map<String, String> customerTaxInfo = new HashMap<>();
            customerTaxInfo.put("type", "vat");
            customerTaxInfo.put("tax_id", pro.getVatcode());
            params.put("tax_info", customerTaxInfo);
        }

        return Customer.create(params);

    }

}
