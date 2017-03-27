/**
 * This file was generated by the JPA Modeler
 */ 

package fr.trendev.comptandye.entity;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * @author jsie
 */

@Entity
public class Client { 

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Basic
    private String email;

    @OneToOne(targetEntity = SocialNetworkAccounts.class)
    private SocialNetworkAccounts socialNetworkAccounts;

    @OneToOne(targetEntity = CustomerDetails.class)
    private CustomerDetails customerDetails;

    @ManyToOne(targetEntity = Address.class)
    private Address address;

    @ManyToOne(targetEntity = Professional.class)
    private Professional professional;

    @OneToMany(targetEntity = ClientBill.class,mappedBy = "client")
    private List<ClientBill> clientBills;

    @ManyToMany(targetEntity = CollectiveGroup.class,mappedBy = "clients")
    private List<CollectiveGroup> collectiveGroups;


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

    public void setSocialNetworkAccounts(SocialNetworkAccounts socialNetworkAccounts) {
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


}
