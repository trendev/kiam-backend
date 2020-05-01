/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.professional.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.bill.entities.Bill;
import fr.trendev.kiam.business.entities.Business;
import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroup;
import fr.trendev.kiam.expense.entities.Expense;
import fr.trendev.kiam.individual.entities.Individual;
import fr.trendev.kiam.notification.entities.Notification;
import fr.trendev.kiam.offering.entities.Offering;
import fr.trendev.kiam.paymentmode.entities.PaymentMode;
import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.useraccount.entities.Customer;
import fr.trendev.kiam.useraccount.entities.UserAccountType;
import fr.trendev.kiam.utils.Visitor;
import fr.trendev.kiam.vatrates.entities.VatRates;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

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
    private String vatcode;

    @Basic
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Basic
    private long billsCount = 0;

    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date billsRefDate;

    @Basic
    private String stripeCustomerId;

    @Basic
    private String stripeSubscriptionId;

    @Basic
    private boolean tos;

    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date rescissionDate;

    @ManyToOne
    private VatRates vatRates;

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Bill> bills = new LinkedList<>();

    @OneToMany
    private List<Business> businesses = new LinkedList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Client> clients = new LinkedList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Offering> offerings = new LinkedList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Category> categories = new LinkedList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Expense> expenses = new LinkedList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CollectiveGroup> collectiveGroups = new LinkedList<>();

    @OneToMany(mappedBy = "professional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Product> stock = new LinkedList<>();

    @OneToMany(mappedBy = "professional", orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Notification> notifications = new LinkedList<>();

    @ManyToMany(mappedBy = "professionals")
    @JsonIgnore
    private List<Individual> individuals = new LinkedList<>();

    @ManyToMany
    private List<PaymentMode> paymentModes = new LinkedList<>();

    @Version
    @JsonIgnore
    private long version = 0l;

    public Professional(String email, String password, String username, String uuid) {
        super(email, password, username, uuid);
        this.cltype = UserAccountType.PROFESSIONAL;
    }

    public Professional() {
        super();
        this.cltype = UserAccountType.PROFESSIONAL;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getVatcode() {
        return vatcode;
    }

    public void setVatcode(String vatcode) {
        this.vatcode = vatcode;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getBillsCount() {
        return billsCount;
    }

    public void setBillsCount(long billsCount) {
        this.billsCount = billsCount;
    }

    public Date getBillsRefDate() {
        return billsRefDate;
    }

    public void setBillsRefDate(Date billsRefDate) {
        this.billsRefDate = billsRefDate;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public void setStripeCustomerId(String stripeCustomerId) {
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeSubscriptionId() {
        return stripeSubscriptionId;
    }

    public void setStripeSubscriptionId(String stripeSubscriptionId) {
        this.stripeSubscriptionId = stripeSubscriptionId;
    }

    public boolean isTos() {
        return tos;
    }

    public void setTos(boolean tos) {
        this.tos = tos;
    }

    public Date getRescissionDate() {
        return rescissionDate;
    }

    public void setRescissionDate(Date rescissionDate) {
        this.rescissionDate = rescissionDate;
    }

    public VatRates getVatRates() {
        return vatRates;
    }

    public void setVatRates(VatRates vatRates) {
        this.vatRates = vatRates;
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Business> getBusinesses() {
        return businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public List<CollectiveGroup> getCollectiveGroups() {
        return collectiveGroups;
    }

    public void setCollectiveGroups(List<CollectiveGroup> collectiveGroups) {
        this.collectiveGroups = collectiveGroups;
    }

    public List<Product> getStock() {
        return stock;
    }

    public void setStock(List<Product> stock) {
        this.stock = stock;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public List<PaymentMode> getPaymentModes() {
        return paymentModes;
    }

    public void setPaymentModes(List<PaymentMode> paymentModes) {
        this.paymentModes = paymentModes;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}