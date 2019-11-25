/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.paymentmode.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.utils.Visitor;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PaymentMode {

    @Id
    private String name;

    public PaymentMode(String name) {
        this.name = name;
    }

    public PaymentMode() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}