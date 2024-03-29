/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.sale.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.offering.entities.Offering;
import fr.trendev.kiam.offering.entities.OfferingType;
import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.utils.Visitor;
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

    @ManyToOne
    private Product product;

    public Sale(String name, int price, int duration, Professional professional) {
        super(name, price, duration, professional);
        this.cltype = OfferingType.SALE;
    }

    public Sale() {
        this.cltype = OfferingType.SALE;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}