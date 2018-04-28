/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.utils.visitors.Visitor;
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
    }

    public Sale() {
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