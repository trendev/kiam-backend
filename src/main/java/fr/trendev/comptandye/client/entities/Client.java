/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.client.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.address.entities.Address;
import fr.trendev.comptandye.category.entities.Category;
import fr.trendev.comptandye.clientbill.entities.ClientBill;
import fr.trendev.comptandye.customerdetails.entities.CustomerDetails;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.utils.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@IdClass(ClientPK.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Client {

    @Id
    @Column(name = "CLIENT_ID")
    @NotNull(message = "Client ID cannot be null")
    private String id;

    @Basic
    private String email;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private SocialNetworkAccounts socialNetworkAccounts;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private CustomerDetails customerDetails;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private Address address;

    @Id
    @ManyToOne
    @JoinColumn(name = "CLIENT_PRO_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ClientBill> clientBills = new LinkedList<>();

    @ManyToMany
    private List<Category> categories = new LinkedList<>();

    public Client(String email, Professional professional) {
        this.email = email;
        this.professional = professional;
        this.customerDetails = new CustomerDetails();
        this.address = new Address();
        this.socialNetworkAccounts = new SocialNetworkAccounts();
    }

    public Client() {
        this.customerDetails = new CustomerDetails();
        this.address = new Address();
        this.socialNetworkAccounts = new SocialNetworkAccounts();
    }

    public Client(String email, SocialNetworkAccounts socialNetworkAccounts, CustomerDetails customerDetails, Address address, Professional professional) {
        this.email = email;
        this.socialNetworkAccounts = socialNetworkAccounts;
        this.customerDetails = customerDetails;
        this.address = address;
        this.professional = professional;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SocialNetworkAccounts getSocialNetworkAccounts() {
        return socialNetworkAccounts;
    }

    public void setSocialNetworkAccounts(SocialNetworkAccounts socialNetworkAccounts) {
        this.socialNetworkAccounts = socialNetworkAccounts;
    }

    public CustomerDetails getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(CustomerDetails customerDetails) {
        this.customerDetails = customerDetails;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<ClientBill> getClientBills() {
        return clientBills;
    }

    public void setClientBills(List<ClientBill> clientBills) {
        this.clientBills = clientBills;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}