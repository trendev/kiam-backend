/**
 * This file was generated by the JPA Modeler
 */
package fr.trendev.comptandye.entities;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * @author jsie
 */
@Entity
public class Professional extends Customer {

    @Basic
    private String website;

    @Basic
    private String business;

    @Basic
    private String companyID;

    @OneToMany(targetEntity = Offering.class, mappedBy = "professional")
    private List<Offering> offerings;

    @OneToMany(targetEntity = Bill.class, mappedBy = "professional")
    private List<Bill> bills;

    @OneToMany(targetEntity = Client.class, mappedBy = "professional")
    private List<Client> clients;

    @OneToMany(targetEntity = CollectiveGroup.class, mappedBy = "professional")
    private List<CollectiveGroup> collectiveGroups;

    @OneToMany(targetEntity = Category.class, mappedBy = "professional")
    private List<Category> categories;

    @OneToMany(targetEntity = Expense.class, mappedBy = "professional")
    private List<Expense> expenses;

    @ManyToMany(targetEntity = Individual.class, mappedBy = "professionals")
    private List<Individual> individuals;

    @ManyToMany(targetEntity = PaymentMode.class)
    private List<PaymentMode> paymentModes;

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getBusiness() {
        return this.business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCompanyID() {
        return this.companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public List<Offering> getOfferings() {
        return this.offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

    public List<Bill> getBills() {
        return this.bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
    }

    public List<Client> getClients() {
        return this.clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
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

    public List<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
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

}