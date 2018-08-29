/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Source;
import fr.trendev.comptandye.entities.Professional;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class StripeCustomerUtils {

    public Customer create(Source source, Professional pro) throws
            StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("source", source.getId());
        params.put("email", pro.getEmail());
        params.put("description", pro.getCustomerDetails().getFirstName()
                + " " + pro.getCustomerDetails().getLastName());

        Map<String, String> customerMetadata = new HashMap<>();
        customerMetadata.put("uuid", pro.getUuid());
        customerMetadata.put("companyName", pro.getCompanyName());
        customerMetadata.put("companyID", pro.getCompanyID());
        params.put("metadata", customerMetadata);

        Map<String, Object> customerShipping = new HashMap<>();
        customerShipping.put("name", pro.getCustomerDetails().getFirstName()
                + " " + pro.getCustomerDetails().getLastName());
        // must be an international format
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
