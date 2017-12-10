/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.utils.visitors.Visitor;
import javax.persistence.Basic;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PurchasedOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * Quantity
     */
    @Basic
    private int qty = 1;

    @Embedded
    private OfferingSnapshot offeringSnapshot = new OfferingSnapshot();

    @ManyToOne(targetEntity = Offering.class)
    private Offering offering;

    public PurchasedOffering() {
    }

    public PurchasedOffering(int qty, Offering offering) {
        this.qty = qty;
        this.offering = null;
        this.offeringSnapshot = new OfferingSnapshot(offering);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public OfferingSnapshot getOfferingSnapshot() {
        return this.offeringSnapshot;
    }

    public void setOfferingSnapshot(OfferingSnapshot offeringSnapshot) {
        this.offeringSnapshot = offeringSnapshot;
    }

    public Offering getOffering() {
        return this.offering;
    }

    public void setOffering(Offering offering) {
        this.offering = offering;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}