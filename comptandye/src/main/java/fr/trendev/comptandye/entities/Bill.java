/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.trendev.comptandye.utils.BillTypeEnum;
import fr.trendev.comptandye.visitors.Visitor;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
@IdClass(BillPK.class)
@DiscriminatorColumn(length = 31)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("unchecked")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cltype",
        visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClientBill.class,
            name = BillTypeEnum.CLIENT_CLTYPE)
    ,   @JsonSubTypes.Type(value = IndividualBill.class,
            name = BillTypeEnum.INDIVIDUAL_CLTYPE)
    ,  @JsonSubTypes.Type(value = CollectiveGroupBill.class,
            name = BillTypeEnum.COLLECTIVEGROUP_CLTYPE)})
public abstract class Bill {

    /**
     * Will be automatically generated based on the deliveryDate, the
     * Professional email and a timestamp.
     */
    @Id
    private String reference;

    @Column(columnDefinition = "DATETIME(3) NOT NULL")
    @Id
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Basic
    private String cltype;

    /**
     * Amount in cents (1/100 of the currency)
     */
    @Basic
    private int amount;

    /**
     * Default value is Euros (EUR)
     */
    @Basic
    private String currency = "EUR";

    /**
     * the discount amount
     */
    @Basic
    private int discount;

    @Column(columnDefinition = "DATETIME(3)")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @ElementCollection
    private List<String> comments = new LinkedList<>();

    @Id
    @ManyToOne(targetEntity = Professional.class)
    @JoinColumn(name = "PROFESSIONAL_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    /**
     * Should be ignored during a PUT
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REMOVE}, targetEntity = Payment.class, orphanRemoval = true)
    private List<Payment> payments = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REMOVE}, targetEntity = PurchasedOffering.class,
            orphanRemoval = true)
    private List<PurchasedOffering> purchasedOfferings = new LinkedList<>();

    public Bill(String reference, Date deliveryDate, int amount, int discount,
            Date paymentDate, List comments, Professional professional,
            List payments, List purchasedOfferings) {
        this.reference = reference;
        this.deliveryDate = deliveryDate;
        this.amount = amount;
        this.discount = discount;
        this.paymentDate = paymentDate;
        this.comments = comments;
        this.professional = professional;
        this.payments = payments;
        this.purchasedOfferings = purchasedOfferings;
    }

    public Bill() {
    }

    public String getReference() {
        return this.reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCltype() {
        return this.cltype;
    }

    public void setCltype(String cltype) {
        this.cltype = cltype;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return this.currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getDiscount() {
        return this.discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public List<String> getComments() {
        return this.comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
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

    public List<PurchasedOffering> getPurchasedOfferings() {
        return this.purchasedOfferings;
    }

    public void setPurchasedOfferings(List<PurchasedOffering> purchasedOfferings) {
        this.purchasedOfferings = purchasedOfferings;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}