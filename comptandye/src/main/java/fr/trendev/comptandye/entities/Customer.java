/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class Customer extends UserAccount {

    @OneToOne(cascade = {CascadeType.ALL}, targetEntity = CustomerDetails.class)
    private CustomerDetails customerDetails;

    @OneToOne(cascade = {CascadeType.ALL}, targetEntity = Address.class)
    private Address address;

    @OneToOne(cascade = {CascadeType.ALL},
            targetEntity = SocialNetworkAccounts.class)
    private SocialNetworkAccounts socialNetworkAccounts;

    public Customer(String email, String password, String username, String uuid) {
        super(email, password, username, uuid);
    }

    public Customer(String email, String password, String username, String uuid,
            CustomerDetails customerDetails, Address address,
            SocialNetworkAccounts socialNetworkAccounts) {
        super(email, password, username, uuid);
        this.customerDetails = customerDetails;
        this.address = address;
        this.socialNetworkAccounts = socialNetworkAccounts;
    }

    public Customer() {
        super();
    }

    public CustomerDetails getCustomerDetails() {
        return this.customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public SocialNetworkAccounts getSocialNetworkAccounts() {
        return this.socialNetworkAccounts;
    }

    public void setSocialNetworkAccounts(
            SocialNetworkAccounts socialNetworkAccounts) {
        this.socialNetworkAccounts = socialNetworkAccounts;
    }

}