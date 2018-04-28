/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.trendev.comptandye.utils.OfferingType;
import fr.trendev.comptandye.utils.visitors.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@IdClass(OfferingPK.class)
@DiscriminatorColumn(length = 31)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cltype",
        visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Service.class, name = OfferingType.SERVICE)
    ,   @JsonSubTypes.Type(value = Pack.class, name = OfferingType.PACK)
    ,  @JsonSubTypes.Type(value = Sale.class, name = OfferingType.SALE)})
public abstract class Offering {

    @Column(name = "OFFERING_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    @NotNull(message = "cltype field in Offering cannot be null")
    private String cltype;

    @Basic
    private String name;

    @Basic
    private String shortname;

    /**
     * reference price, based on Euro cents
     */
    @Basic
    private int price;

    /**
     * Duration (in minutes)
     */
    @Basic
    private int duration;

    @Id
    @ManyToOne(targetEntity = Professional.class)
    @JoinColumn(name = "OFFERING_PRO_EMAIL", referencedColumnName = "EMAIL",
            nullable = false, updatable = false)
    @JsonIgnore
    private Professional professional;

    @OneToMany(targetEntity = Business.class)
    private List<Business> businesses = new LinkedList<>();

    @OneToMany(targetEntity = PurchasedOffering.class, mappedBy = "offering")
    @JsonIgnore
    private List<PurchasedOffering> purchasedOfferings = new LinkedList<>();

    @ManyToMany(targetEntity = Pack.class, mappedBy = "offerings")
    @JsonIgnore
    private List<Pack> parentPacks = new LinkedList<>();

    public Offering(String name, int price, int duration,
            Professional professional) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.professional = professional;
    }

    public Offering() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<Business> getBusinesses() {
        return this.businesses;
    }

    public void setBusinesses(List<Business> businesses) {
        this.businesses = businesses;
    }

    public List<PurchasedOffering> getPurchasedOfferings() {
        return this.purchasedOfferings;
    }

    public void setPurchasedOfferings(List<PurchasedOffering> purchasedOfferings) {
        this.purchasedOfferings = purchasedOfferings;
    }

    public List<Pack> getParentPacks() {
        return this.parentPacks;
    }

    public void setParentPacks(List<Pack> parentPacks) {
        this.parentPacks = parentPacks;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}