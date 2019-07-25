/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.Source;
import fr.trendev.comptandye.professional.entities.Professional;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.ejb.Singleton;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
@Singleton
public class StripeCustomerController {

    /**
     * Creates a Stripe Customer from a Stripe Source, the data of the
     * Professional.
     *
     * @param token the id of the Stripe Token
     * @param pro the Professional
     * @return the new Stripe Customer
     * @throws StripeException if errors occur from Stripe services
     */
    public Customer create(String token, Professional pro) throws
            StripeException {
        Map<String, Object> params = new HashMap<>();

        params.put("source", token);

        Map<String, Object> invoicesSettings = new HashMap<>();
        invoicesSettings.put("footer", "comptandye"); // add TRENDev SASU legals
        params.put("invoice_settings", invoicesSettings);

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
        if (pro.getAddress().getOptional() != null
                && !pro.getAddress().getOptional().isEmpty()) {
            customerShippingAddress.put("line2", pro.getAddress().getOptional());
        }
        customerShippingAddress.put("city", pro.getAddress().getCity());
        customerShippingAddress.
                put("country", pro.getAddress().getCountry());
        customerShippingAddress.
                put("postal_code", pro.getAddress().getPostalCode());
        customerShipping.put("address", customerShippingAddress);
        params.put("shipping", customerShipping);

        if (pro.getVatcode() != null && !pro.getVatcode().isEmpty()) {
            Map<String, Object> taxID = new HashMap<>();
            taxID.put("type", "eu_vat");
            taxID.put("value", pro.getVatcode());
            Map<String, Object> taxIDs = new HashMap<>();
            taxIDs.put("0", taxID);
            params.put("tax_id_data", taxIDs);
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
                    + " has no Customer id !");
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
        Map<String, Object> params = new HashMap<>();
        params.put("source", sourceId);
        customer.getSources().create(params);
        params = new HashMap<>();
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
        Map<String, Object> params = new HashMap<>();
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
     * Professional.
     *
     * @param pro the Professional
     * @return the Stripe Invoice list
     * @throws StripeException if errors occur from the Stripe services
     */
    public InvoiceCollection getInvoices(Professional pro) throws
            StripeException {

        Map<String, Object> invoiceParams = new HashMap<>();
        invoiceParams.put("limit", 100);
        invoiceParams.put("customer", pro.getStripeCustomerId());

        List<Invoice> list = new LinkedList<>();

        /**
         * Gather the Stripe Invoices using the auto paging iteration. It will
         * request ALL invoices compared to Invoice.list() which request only
         * 100 max invoices.
         */
        for (Invoice i : Invoice.list(invoiceParams).autoPagingIterable()) {
            list.add(i);
        }

        /**
         * For JSON serialization purposes, use an InvoiceCollection instead of
         * returning a List<Invoice>. Invoices will be serialized and the fields
         * won't fit with the Stripe JSON serialization. Ex: amount_due becomes
         * amountDue.
         */
        InvoiceCollection invoices = new InvoiceCollection();
        invoices.setData(list);

        return invoices;
    }
}
