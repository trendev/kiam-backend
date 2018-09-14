/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.Source;
import fr.trendev.comptandye.entities.Professional;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Singleton;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
@Singleton
public class StripeCustomerUtils {

    /**
     * Creates a Stripe Customer from a Stripe Source, the data of the
     * Professional.
     *
     * @param sourceId the id of the Stripe Source
     * @param pro the Professional
     * @return the new Stripe Customer
     * @throws StripeException if errors occur from Stripe services
     */
    public Customer create(String sourceId, Professional pro) throws
            StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("source", sourceId);
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

    /**
     * Gets details from the Stripe Customer config of a Professional
     *
     * @param pro the Professional
     * @return the Stripe Customer (as JSON object)
     * @throws StripeException if errors occur from Stripe
     */
    public Customer details(Professional pro) throws StripeException {
        return this.retrieveCustomer(pro);
    }

    private Customer retrieveCustomer(Professional pro) throws StripeException {
        String id = pro.getStripeCustomerId();

        if (id == null || id.isEmpty()) {
            throw new WebApplicationException(
                    "Error retrieving a Stripe Customer: Professional "
                    + pro.getEmail()
                    + "has no Customer id !");
        } else {
            return Customer.retrieve(id);
        }
    }

    /**
     * Adds a new Source and sets it as the default one.
     *
     * @param sourceId the id of the Stripe Source
     * @param pro the Professional
     * @return the updated Stripe Customer
     * @throws StripeException if errors occur from Stripe
     */
    public Customer addSource(String sourceId, Professional pro) throws
            StripeException {
        Customer customer = this.retrieveCustomer(pro);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("source", sourceId);
        customer.getSources().create(params);
        params = new HashMap<String, Object>();
        params.put("default_source", sourceId);
        return customer.update(params);
    }

    /**
     * Sets a Stripe source as the default one.
     *
     * @param sourceId the id of the Stripe Source
     * @param pro the Professional
     * @return the updated Stripe Customer
     * @throws StripeException if errors occur from Stripe
     */
    public Customer defaultSource(String sourceId, Professional pro) throws
            StripeException {
        Customer customer = this.retrieveCustomer(pro);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("default_source", sourceId);
        return customer.update(params);
    }

    /**
     * Detaches (removes) a Stripe Source from a Stripe Customer.
     *
     * @param sourceId
     * @param pro
     * @return
     * @throws StripeException
     */
    public Customer detachSource(String sourceId, Professional pro) throws
            StripeException {
        Source source = Source.retrieve(sourceId);
        source.detach();// will set the source status with consumed (cannot be used anymore)
        Customer customer = this.retrieveCustomer(pro);
        return customer;
    }

    /**
     * Provides the Stripe Invoices of the Stripe Customer linked to the
     * Professional. Retention period is 5 years. Stripe limits the response to
     * a max of 100 invoices.
     *
     * @param pro the Professional
     * @return the Stripe Invoice list
     * @throws StripeException if errors occur from the Stripe services
     */
    public InvoiceCollection getInvoices(Professional pro) throws
            StripeException {

        int r = 5; // retention period of 5 years 
        int n = r * 12;
        Map<String, Object> invoiceParams = new HashMap<String, Object>();
        invoiceParams.put("limit", (n > 100) ? 100 : n);
        invoiceParams.put("customer", pro.getStripeCustomerId());
//        invoiceParams.put("subscription", pro.getStripeSubscriptionId());

        return Invoice.list(invoiceParams);
    }
}
