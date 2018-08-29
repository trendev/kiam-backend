/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Subscription;
import fr.trendev.comptandye.entities.Professional;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class StripeSubscriptionUtils {

    public Subscription createDefaultSubscription(Customer customer,
            Professional pro) throws StripeException {
        Map<String, Object> item = new HashMap<>();
        item.put("plan", "classic");
        Map<String, Object> items = new HashMap<>();
        items.put("0", item);
        Map<String, Object> params = new HashMap<>();
        params.put("customer", customer.getId());
        params.put("items", items);
        params.put("tax_percent", 20);

        if (pro.getUserGroups().stream().anyMatch(grp -> grp.getName().
                equals("Ambassador"))) {
            params.put("coupon", "Ambassador");
        }

        return Subscription.create(params);
    }

}
