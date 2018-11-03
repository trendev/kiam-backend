/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.address.entities.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.utils.visitors.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author jsie
 */
@Entity
@Table(name = "COLLECTIVE_GROUP")
@IdClass(CollectiveGroupPK.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CollectiveGroup {

    @Column(name = "COLLECTIVE_GROUP_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String groupName;

    @Basic
    private String phone;

    @OneToOne(orphanRemoval = true, cascade = {CascadeType.ALL},
            targetEntity = Address.class)
    private Address address;

    @Id
    @ManyToOne(targetEntity = Professional.class)
    @JoinColumn(name = "COLLECTIVE_GROUP_PRO_EMAIL",
            referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    @OneToMany(cascade = {CascadeType.ALL},
            targetEntity = CollectiveGroupBill.class,
            mappedBy = "collectiveGroup")
    @JsonIgnore
    private List<CollectiveGroupBill> collectiveGroupBills = new LinkedList<>();

    public CollectiveGroup(String groupName, String phone,
            Professional professional) {
        this.groupName = groupName;
        this.phone = phone;
        this.professional = professional;
        this.address = new Address();
    }

    public CollectiveGroup(String groupName, String phone, Address address,
            Professional professional) {
        this.groupName = groupName;
        this.phone = phone;
        this.address = address;
        this.professional = professional;
    }

    public CollectiveGroup() {
        this.address = new Address();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Professional getProfessional() {
        return this.professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<CollectiveGroupBill> getCollectiveGroupBills() {
        return this.collectiveGroupBills;
    }

    public void setCollectiveGroupBills(
            List<CollectiveGroupBill> collectiveGroupBills) {
        this.collectiveGroupBills = collectiveGroupBills;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}