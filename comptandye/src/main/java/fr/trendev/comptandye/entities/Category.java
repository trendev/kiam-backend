/**
 * This file was generated by the JPA Modeler
 */
package fr.trendev.comptandye.entities;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 * @author jsie
 */
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String description;

    @Basic
    private String name;

    @ManyToOne(targetEntity = Professional.class)
    private Professional professional;

    @OneToMany(targetEntity = Client.class, mappedBy = "category")
    private List<Client> clients;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<Client> getClients() {
        return this.clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

}