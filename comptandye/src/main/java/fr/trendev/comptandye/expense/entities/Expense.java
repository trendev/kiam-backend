/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.expense.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.classicexpense.entities.ClassicExpense;
import fr.trendev.comptandye.expenseitem.entities.ExpenseItem;
import fr.trendev.comptandye.payment.entities.Payment;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.purchaseexpense.entities.PurchaseExpense;
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

/**
 * @author jsie
 */
@Entity
@IdClass(ExpensePK.class)
@DiscriminatorColumn(length = 31)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@SuppressWarnings("unchecked")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cltype",
        visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClassicExpense.class,
            name = ExpenseType.CLASSIC_EXPENSE)
    ,   @JsonSubTypes.Type(value = PurchaseExpense.class,
            name = ExpenseType.PURCHASE_EXPENSE)})
public abstract class Expense {

    @Column(name = "EXPENSE_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @NotNull(message = "cltype field in Expense must not be null")
    protected String cltype;

    @Basic
    @NotNull(message = "description field in Expense must not be null")
    private String description;

    /**
     * Amount in cents (1/100 of the currency)
     */
    @Basic
    private int amount;

    /**
     * Default value is Euros (EUR)
     */
    @Basic
    @NotNull(message = "currency field in Expense must not be null")
    private String currency = "EUR";

    @Column(columnDefinition = "DATETIME(3)")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "paymentDate field in Expense must not be null")
    @Past(message = "paymentDate field in Expense must not be a futur date")
    private Date paymentDate;

    /**
     * used for audit and sort the bills
     */
    @Column(columnDefinition = "DATETIME(3)")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "issueDate field in Expense must not be null")
    private Date issueDate = new Date();

    @Basic
    @NotNull(message = "provider field in Expense must not be null")
    private String provider;

    /**
     * mark if an expense is cancelled or not
     */
    @Basic
    private boolean cancelled = false;

    @Column(columnDefinition = "DATETIME(3)")
    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancellationDate;

    /**
     * mark if the expense is vat inclusive or not
     */
    @Basic
    private boolean vatInclusive = false;

    @ElementCollection
    @NotNull(message = "comments field in Expense must not be null")
    private List<String> categories = new LinkedList<>();

    @Id
    @ManyToOne(targetEntity = Professional.class)
    @JoinColumn(name = "EXPENSE_PRO_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    /**
     * Should be ignored during a PUT
     */
    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = Payment.class,
            orphanRemoval = true)
    @NotNull(message = "payments field in Expense must not be null")
    private List<Payment> payments = new LinkedList<>();

    @OneToMany(targetEntity = Business.class)
    @NotNull(message = "businesses field in Expense must not be null")
    private List<Business> businesses = new LinkedList<>();

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = ExpenseItem.class,
            orphanRemoval = true)
    private List<ExpenseItem> expenseItems = new LinkedList<>();

    public Expense() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCltype() {
        return this.cltype;
    }

    public void setCltype(String cltype) {
        this.cltype = cltype;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Date getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getIssueDate() {
        return this.issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Date getCancellationDate() {
        return this.cancellationDate;
    }

    public void setCancellationDate(Date cancellationDate) {
        this.cancellationDate = cancellationDate;
    }

    public boolean isVatInclusive() {
        return this.vatInclusive;
    }

    public void setVatInclusive(boolean vatInclusive) {
        this.vatInclusive = vatInclusive;
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

    public List<ExpenseItem> getExpenseItems() {
        return this.expenseItems;
    }

    public void setExpenseItems(List<ExpenseItem> expenseItems) {
        this.expenseItems = expenseItems;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}
