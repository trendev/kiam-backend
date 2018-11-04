/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.sale.entities;

import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.offering.entities.Offering;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.offering.entities.OfferingType;
import fr.trendev.comptandye.utils.Visitor;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Sale extends Offering {

    @Basic
    private int qty;

    @ManyToOne(targetEntity = Product.class)
    private Product product;

    public Sale(String name, int price, int duration, Professional professional) {
        super(name, price, duration, professional);
        this.cltype = OfferingType.SALE;
    }

    public Sale() {
        this.cltype = OfferingType.SALE;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}