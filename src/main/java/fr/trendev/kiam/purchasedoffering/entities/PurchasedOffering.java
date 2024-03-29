/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.purchasedoffering.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.offering.entities.Offering;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PurchasedOffering {

    @Id
    @NotNull(message = "PurchasedOffering ID cannot be null")
    private String id;

    @Basic
    private int qty = 1;

    @Basic
    @Column(updatable = false, scale = 2, precision = 5)
    private BigDecimal vatRate;

    @Embedded
    private OfferingSnapshot offeringSnapshot = new OfferingSnapshot();

    @Embedded
    private OfferingExtents offeringExtents;

    @ManyToOne
    private Offering offering;

    public PurchasedOffering() {
    }

    public PurchasedOffering(int qty, Offering offering) {
        this.qty = qty;
        this.offering = offering;
        this.offeringSnapshot = new OfferingSnapshot(offering);
        this.offeringExtents = new OfferingExtents(offering);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getVatRate() {
        return vatRate;
    }

    public void setVatRate(BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    public OfferingSnapshot getOfferingSnapshot() {
        return offeringSnapshot;
    }

    public void setOfferingSnapshot(OfferingSnapshot offeringSnapshot) {
        this.offeringSnapshot = offeringSnapshot;
    }

    public OfferingExtents getOfferingExtents() {
        return offeringExtents;
    }

    public void setOfferingExtents(OfferingExtents offeringExtents) {
        this.offeringExtents = offeringExtents;
    }

    public Offering getOffering() {
        return offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

}