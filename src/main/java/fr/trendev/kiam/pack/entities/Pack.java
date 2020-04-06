/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.pack.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.offering.entities.Offering;
import fr.trendev.kiam.offering.entities.OfferingType;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.utils.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pack extends Offering {

    @ManyToMany
    @JoinTable(name = "PACK_CONTENT", joinColumns = {
        @JoinColumn(name = "PACK_ID", referencedColumnName = "OFFERING_ID", table = "OFFERING"),
        @JoinColumn(name = "PACK_PROFESSIONAL_EMAIL", referencedColumnName = "OFFERING_PRO_EMAIL", table = "OFFERING")})
    private List<Offering> offerings = new LinkedList<>();

    public Pack(String name, int price, int duration, Professional professional) {
        super(name, price, duration, professional);
        this.cltype = OfferingType.PACK;
    }

    public Pack() {
        this.cltype = OfferingType.PACK;
    }

    public List<Offering> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}