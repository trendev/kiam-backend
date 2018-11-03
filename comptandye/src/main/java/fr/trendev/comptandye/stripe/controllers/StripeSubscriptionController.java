/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import fr.trendev.comptandye.professional.entities.Professional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class StripeSubscriptionController {

    /**
     * Creates a default Stripe Subscription to Stripe Plan "classic", adds a
     * Stripe Coupon (100% free) and adds to the Stripe Customer
     *
     * @param customer the Stripe Customer created for the Professional
     * @param pro the Professional
     * @return the active Stripe Subscription to the Stripe Plan "classic"
     * @throws StripeException if errors occur from the Stripe services
     */
    public Subscription createDefaultSubscription(Customer customer,
            Professional pro) throws StripeException {
        Map<String, Object> item = new HashMap<>();
        item.put("plan", "classic");
        Map<String, Object> items = new HashMap<>();
        items.put("0", item);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customer.getId());
        params.put("items", items);
        params.put("tax_percent", 20);// VAT in France for web services

        // checks if Professional is in the Ambassador User Group
        // if yes, the Subscription is linked with a discount (100% free)
        if (pro.getUserGroups().stream().anyMatch(grp -> grp.getName().
                equals("Ambassador"))) {
            params.put("coupon", "Ambassador");
        }

        return Subscription.create(params);
    }

    /**
     * Manages the rescission
     *
     * @param pro the Professional
     * @param rescind true if the subscription must be rescind, false if the
     * subscription must be reactivated
     * @return the rescinded/reactivated subscription
     * @throws StripeException if errors occur from the Stripe services
     */
    private Subscription manageRescission(Professional pro,
            boolean rescind)
            throws StripeException {
        Subscription subscription = Subscription.retrieve(
                pro.getStripeSubscriptionId());
        Map<String, Object> params = new HashMap<>();
        params.put("cancel_at_period_end", rescind);
        pro.setRescissionDate(
                rescind ? new Date(subscription.getCurrentPeriodEnd() * 1000) : null
        );
        return subscription.update(params);
    }

    /**
     * Rescinds an existing Stripe Subscription
     *
     * @param pro the Professional who wants to cancel its subscription
     * @return the rescinded subscription
     * @throws StripeException if errors occur from the Stripe services
     */
    public Subscription rescind(Professional pro) throws StripeException {
        return this.manageRescission(pro, true);
    }

    /**
     * Reactivates an existing Stripe Subscription
     *
     * @param pro the Professional who wants to reactivate its canceled
     * subscription
     * @return the reactivated subscription
     * @throws StripeException if errors occur from the Stripe services
     */
    public Subscription reactivate(Professional pro) throws StripeException {
        return this.manageRescission(pro, false);
    }

}
