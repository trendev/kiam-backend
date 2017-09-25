/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.visitors.Visitor;
import javax.persistence.Entity;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Service extends Offering {

    public Service(String name, int price, int duration,
            Professional professional) {
        super(name, price, duration, professional);
    }

    public Service() {
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

}