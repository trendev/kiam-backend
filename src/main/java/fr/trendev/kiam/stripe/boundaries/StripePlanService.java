/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe.boundaries;

import com.stripe.exception.StripeException;
import com.stripe.model.Plan;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("stripe-plan")
@RolesAllowed({"Administrator", "Professional"})
public class StripePlanService {

    private static final Logger LOG = Logger.getLogger(StripePlanService.class.getName());

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlans() {
        try {
            Map<String, Object> planParams = new HashMap<>();
            planParams.put("active", true);
            return Response.ok(Plan.list(planParams).toJson()).build();
        } catch (StripeException ex) {
            throw new WebApplicationException(
                    "Exception occurs returning a list of Stripe Plan", ex);
        }
    }

}
