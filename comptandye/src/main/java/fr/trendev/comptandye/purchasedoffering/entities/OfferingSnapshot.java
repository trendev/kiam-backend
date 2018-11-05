/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.purchasedoffering.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.offering.entities.Offering;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

/**
 * @author jsie
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OfferingSnapshot {

    @Basic
    private String cltype;

    @Basic
    private String name;

    @Basic
    private String shortname;

    /**
     * Price in cents (1/100 of the currency)
     */
    @Basic
    private int price;

    /**
     * Duration (in minutes)
     */
    @Basic
    private int duration;

    @OneToMany(targetEntity = Business.class)
    private List<Business> businesses = new LinkedList<>();

    public OfferingSnapshot() {
    }

    public OfferingSnapshot(Offering offering) {
        this.cltype = offering.getCltype();
        this.name = offering.getName();
        this.shortname = offering.getShortname();
        this.price = offering.getPrice();
        this.duration = offering.getDuration();
        this.businesses = offering.getBusinesses();
    }

    public String getCltype() {
        return this.cltype;
    }

    public void setCltype(String cltype) {
        this.cltype = cltype;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return this.shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public int getPrice() {
        return this.price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Business> getBusinesses() {
        return this.businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

}