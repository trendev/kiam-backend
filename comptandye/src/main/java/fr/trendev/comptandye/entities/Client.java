/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.visitors.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * @author jsie
 */
@Entity
@IdClass(ClientPK.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Client {

    @Column(name = "CLIENT_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String email;

    @OneToOne(orphanRemoval = true, cascade = {CascadeType.ALL},
            targetEntity = SocialNetworkAccounts.class)
    private SocialNetworkAccounts socialNetworkAccounts;

    @OneToOne(orphanRemoval = true, cascade = {CascadeType.ALL},
            targetEntity = CustomerDetails.class)
    private CustomerDetails customerDetails;

    @OneToOne(orphanRemoval = true, cascade = {CascadeType.ALL},
            targetEntity = Address.class)
    private Address address;

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = Professional.class)
    @JoinColumn(name = "CLIENT_PRO_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = ClientBill.class,
            mappedBy = "client")
    @JsonIgnore
    private List<ClientBill> clientBills = new LinkedList<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = CollectiveGroup.class,
            mappedBy = "clients")
    @JsonIgnore
    private List<CollectiveGroup> collectiveGroups = new LinkedList<>();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = Category.class)
    @JsonIgnore
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

    public Client(String email, SocialNetworkAccounts socialNetworkAccounts,
            CustomerDetails customerDetails, Address address,
            Professional professional) {
        this.email = email;
        this.socialNetworkAccounts = socialNetworkAccounts;
        this.customerDetails = customerDetails;
        this.address = address;
        this.professional = professional;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SocialNetworkAccounts getSocialNetworkAccounts() {
        return this.socialNetworkAccounts;
    }

    public void setSocialNetworkAccounts(
            SocialNetworkAccounts socialNetworkAccounts) {
        this.socialNetworkAccounts = socialNetworkAccounts;
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

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<ClientBill> getClientBills() {
        return this.clientBills;
    }

    public void setClientBills(List<ClientBill> clientBills) {
        this.clientBills = clientBills;
    }

    public List<CollectiveGroup> getCollectiveGroups() {
        return this.collectiveGroups;
    }

    public void setCollectiveGroups(List<CollectiveGroup> collectiveGroups) {
        this.collectiveGroups = collectiveGroups;
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}