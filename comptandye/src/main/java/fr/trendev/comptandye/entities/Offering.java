/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@IdClass(OfferingPK.class)
@DiscriminatorColumn(length = 31)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class Offering {

    @Column(name = "OFFERING_ID", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

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

    @Id
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = Professional.class)
    @JoinColumn(name = "OFFERING_PRO_EMAIL", referencedColumnName = "EMAIL",
            nullable = false)
    @JsonIgnore
    private Professional professionalFromOffering;

    public Offering(String name, int price, int duration,
            Professional professionalFromOffering) {
        this.name = name;
        this.price = price;
        this.duration = duration;
        this.professionalFromOffering = professionalFromOffering;
    }

    public Offering() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Professional getProfessionalFromOffering() {
        return this.professionalFromOffering;
    }

    public void setProfessionalFromOffering(
            Professional professionalFromOffering) {
        this.professionalFromOffering = professionalFromOffering;
    }

}