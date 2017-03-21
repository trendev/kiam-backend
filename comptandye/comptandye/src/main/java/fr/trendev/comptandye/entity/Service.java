/**
 * This file was generated by the JPA Modeler
 */ 

package fr.trendev.comptandye.entity;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 * @author jsie
 */

@Entity
public class Service { 

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

    @Basic
    private long price;

    @Basic
    private int duration;

    @ManyToMany(targetEntity = ServiceSet.class,mappedBy = "services")
    private List<ServiceSet> serviceSets;


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

    public long getPrice() {
        return this.price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<ServiceSet> getServiceSets() {
        return this.serviceSets;
    }

    public void setServiceSets(List<ServiceSet> serviceSets) {
        this.serviceSets = serviceSets;
    }


}
