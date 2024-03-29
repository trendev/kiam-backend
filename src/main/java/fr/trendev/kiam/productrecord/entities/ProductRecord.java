/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.productrecord.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.purchaseditem.entities.PurchasedItem;
import fr.trendev.kiam.returneditem.entities.ReturnedItem;
import fr.trendev.kiam.solditem.entities.SoldItem;
import fr.trendev.kiam.useditem.entities.UsedItem;
import fr.trendev.kiam.utils.Visitor;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cltype", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PurchasedItem.class, name = ProductRecordType.PURCHASED_ITEM),
    @JsonSubTypes.Type(value = UsedItem.class, name = ProductRecordType.USED_ITEM),
    @JsonSubTypes.Type(value = ReturnedItem.class, name = ProductRecordType.RETURNED_ITEM),
    @JsonSubTypes.Type(value = SoldItem.class, name = ProductRecordType.SOLD_ITEM)})
public abstract class ProductRecord {

    @Id
    @NotNull(message = "ProductRecord ID cannot be null")
    private String id;

    @Basic
    @NotNull(message = "cltype field in ProductRecord cannot be null")
    protected String cltype;

    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "recordDate field in ProductRecard cannot be null")
    private Date recordDate = new Date();

    @Basic
    private int qty;

    @Basic
    private boolean cancelled = false;

    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cancellationDate;

    @ManyToOne
    private Product product;

    public ProductRecord(String cltype, Date recordDate, int qty) {
        this.cltype = cltype;
        this.recordDate = recordDate;
        this.qty = qty;
    }

    public ProductRecord() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCltype() {
        return cltype;
    }

    public void setCltype(String cltype) {
        this.cltype = cltype;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

    public void cancel(VariationOfProductRecordQtyVisitor visitor) {
        this.cancelled = true;
        this.cancellationDate = new Date();

        int availableQty = this.product.getAvailableQty() - this.accept(visitor);
        this.product.setAvailableQty(availableQty < 0 ? 0 : availableQty);
    }

}