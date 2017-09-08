/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Pack extends Offering {

    /**
     * TODO : recursively check that all services and packs are owned by the
     * same Professional.
     */
    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = Offering.class)
    private List<Offering> offerings = new LinkedList<>();

    public Pack(String name, int price, int duration,
            Professional professionalFromOffering) {
        super(name, price, duration, professionalFromOffering);
    }

    public Pack() {
    }

    public List<Offering> getOfferings() {
        return this.offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

}