/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.productreference.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.business.entities.Business;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductReference {

    @Id
    @Column(nullable = false, updatable = false)
    private String barcode;

    @Basic
    @NotNull(message = "description field in ProductReference must not be null")
    private String description;

    @Basic
    @NotNull(message = "brand field in ProductReference must not be null")
    private String brand;

    @OneToOne
    @NotNull(message = "business field in ProductReference must not be null")
    private Business business;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

}