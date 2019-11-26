/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.purchasedoffering.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.offering.entities.Offering;
import fr.trendev.comptandye.offering.entities.OfferingExtentsVisitor;
import javax.persistence.Basic;
import javax.persistence.Embeddable;

/**
 * @author jsie
 */
@Embeddable
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OfferingExtents {

    /**
     * Extent of the Services
     */
    @Basic
    private int services;

    /**
     * Extent of the Sales
     */
    @Basic
    private int sales;

    public OfferingExtents() {
    }

    public OfferingExtents(int services, int sales) {
        this.services = services;
        this.sales = sales;
    }

    public OfferingExtents(Offering offering) {
        //computes services/sales extents
        OfferingExtents oe = offering.accept(new OfferingExtentsVisitor());
        this.services = oe.services;
        this.sales = oe.sales;
    }

    public int getServices() {
        return services;
    }

    public void setServices(int services) {
        this.services = services;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

}