/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.utils.visitors.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@IdClass(CategoryPK.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Category {

    @Column(name = "CATEGORY_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String description;

    @Basic
    private String name;

    @Id
    @ManyToOne(targetEntity = Professional.class)
    @JoinColumn(name = "CATEGORY_PRO_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    @ManyToMany(targetEntity = Client.class, mappedBy = "categories")
    @JsonIgnore
    private List<Client> clients = new LinkedList<>();

    public Category(String description, String name, Professional professional) {
        this.description = description;
        this.name = name;
        this.professional = professional;
    }

    public Category() {
    }

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

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}