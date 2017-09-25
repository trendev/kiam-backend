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
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author jsie
 */
@Entity
@IdClass(ExpensePK.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("unchecked")
public class Expense {

    @Column(name = "EXPENSE_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

    /**
     * Amount in cents (1/100 of the currency)
     */
    @Basic
    private int amount;

    @Column(columnDefinition = "DATETIME(3)")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Basic
    private String invoiceRef;

    @ElementCollection
    private List<String> categories = new LinkedList<>();

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = Professional.class)
    @JoinColumn(name = "EXPENSE_PRO_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    /**
     * Should be ignored during a PUT
     */
    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Payment.class,
            orphanRemoval = true)
    private List<Payment> payments = new LinkedList<>();

    @OneToMany(targetEntity = Business.class)
    private List<Business> businesses = new LinkedList<>();

    public Expense(String name, int amount, Date paymentDate, String invoiceRef,
            List categories, Professional professional, List payments,
            List businesses) {
        this.name = name;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.invoiceRef = invoiceRef;
        this.categories = categories;
        this.professional = professional;
        this.payments = payments;
        this.businesses = businesses;
    }

    public Expense() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getInvoiceRef() {
        return this.invoiceRef;
    }

    public void setInvoiceRef(String invoiceRef) {
        this.invoiceRef = invoiceRef;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<Business> getBusinesses() {
        return this.businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

}