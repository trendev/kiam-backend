/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.bill.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.trendev.comptandye.clientbill.entities.ClientBill;
import fr.trendev.comptandye.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.comptandye.individualbill.entities.IndividualBill;
import fr.trendev.comptandye.payment.entities.Payment;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.comptandye.utils.Visitor;
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
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@IdClass(BillPK.class)
@DiscriminatorColumn(length = 31)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cltype", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClientBill.class, name = BillType.CLIENT),
    @JsonSubTypes.Type(value = IndividualBill.class, name = BillType.INDIVIDUAL),
    @JsonSubTypes.Type(value = CollectiveGroupBill.class, name = BillType.COLLECTIVEGROUP)})
public abstract class Bill {

    /**
     * Will be automatically generated based on the deliveryDate, the
     * Professional email and a timestamp.
     */
    @Id
    private String reference;

    @Id
    @Column(columnDefinition = "DATETIME(3) NOT NULL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deliveryDate;

    @Basic
    @NotNull(message = "cltype field in Bill cannot be null")
    protected String cltype;

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

    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    /**
     * used for audit and sort the bills
     */
    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issueDate = new Date();

    /**
     * mark if the bill is vat inclusive or not
     */
    @Basic
    private boolean vatInclusive = false;

    /**
     * mark if a bill is cancelled or not
     */
    @Basic
    private boolean cancelled = false;

    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancellationDate;

    @ElementCollection
    private List<String> comments = new LinkedList<>();

    @Id
    @ManyToOne
    @JoinColumn(name = "PROFESSIONAL_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    /**
     * Should be ignored during a PUT
     */
    @OneToMany(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Payment> payments = new LinkedList<>();

    @OneToMany(orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
    private List<PurchasedOffering> purchasedOfferings = new LinkedList<>();

    public Bill(String reference, Date deliveryDate, int amount, int discount, Date paymentDate, Professional professional) {
        this.reference = reference;
        this.deliveryDate = deliveryDate;
        this.amount = amount;
        this.discount = discount;
        this.paymentDate = paymentDate;
        this.professional = professional;
    }

    public Bill() {
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getCltype() {
        return cltype;
    }

    public void setCltype(String cltype) {
        this.cltype = cltype;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public boolean isVatInclusive() {
        return vatInclusive;
    }

    public void setVatInclusive(boolean vatInclusive) {
        this.vatInclusive = vatInclusive;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Date getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<PurchasedOffering> getPurchasedOfferings() {
        return purchasedOfferings;
    }

    public void setPurchasedOfferings(List<PurchasedOffering> purchasedOfferings) {
        this.purchasedOfferings = purchasedOfferings;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}