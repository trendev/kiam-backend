/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author jsie
 */
@Entity
@IdClass(ProductPK.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Product {

    /**
     * Quantity of available items
     */
    @Basic
    private int availableQty;

    @Basic
    private int thresholdWarning;

    @Basic
    private int thresholdSevere;

    @ElementCollection
    private List<String> comments = new LinkedList<>();

    @Id
    @ManyToOne(targetEntity = Professional.class)
    @JoinColumn(name = "PRODUCT_PRO_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    @Id
    @ManyToOne(cascade = {CascadeType.PERSIST},
            targetEntity = ProductReference.class)
    private ProductReference productReference;

    @OneToMany(targetEntity = Sale.class, mappedBy = "product")
    @JsonIgnore
    private List<Sale> sales = new LinkedList<>();

    @OneToMany(targetEntity = ProductRecord.class, mappedBy = "product")
    @JsonIgnore
    private List<ProductRecord> productRecords = new LinkedList<>();

    public int getAvailableQty() {
        return this.availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    public int getThresholdWarning() {
        return this.thresholdWarning;
    }

    public void setThresholdWarning(int thresholdWarning) {
        this.thresholdWarning = thresholdWarning;
    }

    public int getThresholdSevere() {
        return this.thresholdSevere;
    }

    public void setThresholdSevere(int thresholdSevere) {
        this.thresholdSevere = thresholdSevere;
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

    public ProductReference getProductReference() {
        return this.productReference;
    }

    public void setProductReference(ProductReference productReference) {
        this.productReference = productReference;
    }

    public List<Sale> getSales() {
        return this.sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<ProductRecord> getProductRecords() {
        return this.productRecords;
    }

    public void setProductRecords(List<ProductRecord> productRecords) {
        this.productRecords = productRecords;
    }

}