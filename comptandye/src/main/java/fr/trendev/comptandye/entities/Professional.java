/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.visitors.Visitor;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Professional extends Customer {

    @Basic
    private String website;

    @Basic
    private String companyName;

    @Basic
    private String companyID;

    @Basic
    private String VATcode;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Bill.class,
            mappedBy = "professional")
    @JsonIgnore
    private List<Bill> bills = new LinkedList<>();

    @OneToMany(targetEntity = Business.class)
    private List<Business> businesses = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Client.class,
            mappedBy = "professional")
    @JsonIgnore
    private List<Client> clients = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Offering.class,
            mappedBy = "professional")
    @JsonIgnore
    private List<Offering> offerings = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Category.class,
            mappedBy = "professional")
    @JsonIgnore
    private List<Category> categories = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Expense.class,
            mappedBy = "professional")
    @JsonIgnore
    private List<Expense> expenses = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = CollectiveGroup.class,
            mappedBy = "professional")
    @JsonIgnore
    private List<CollectiveGroup> collectiveGroups = new LinkedList<>();

    @ManyToMany(targetEntity = Individual.class, mappedBy = "professionals")
    @JsonIgnore
    private List<Individual> individuals = new LinkedList<>();

    @ManyToMany(targetEntity = PaymentMode.class)
    private List<PaymentMode> paymentModes = new LinkedList<>();

    public Professional(String email, String password, String username,
            String uuid) {
        super(email, password, username, uuid);
    }

    public Professional() {
        super();
    }

    public Professional(String email, String password, String username,
            String uuid, CustomerDetails customerDetails, Address address,
            SocialNetworkAccounts socialNetworkAccounts, String website,
            String companyName, String companyID, String VATcode,
            Date creationDate) {
        super(email, password, username, uuid, customerDetails, address,
                socialNetworkAccounts);
        this.website = website;
        this.companyName = companyName;
        this.companyID = companyID;
        this.VATcode = VATcode;
        this.creationDate = creationDate;
    }

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyID() {
        return this.companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getVATcode() {
        return this.VATcode;
    }

    public void setVATcode(String VATcode) {
        this.VATcode = VATcode;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Bill> getBills() {
        return this.bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Business> getBusinesses() {
        return this.businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public List<Client> getClients() {
        return this.clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Offering> getOfferings() {
        return this.offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<CollectiveGroup> getCollectiveGroups() {
        return this.collectiveGroups;
    }

    public void setCollectiveGroups(List<CollectiveGroup> collectiveGroups) {
        this.collectiveGroups = collectiveGroups;
    }

    public List<Individual> getIndividuals() {
        return this.individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public List<PaymentMode> getPaymentModes() {
        return this.paymentModes;
    }

    public void setPaymentModes(List<PaymentMode> paymentModes) {
        this.paymentModes = paymentModes;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

}