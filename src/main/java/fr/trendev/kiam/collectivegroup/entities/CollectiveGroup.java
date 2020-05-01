/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.collectivegroup.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.address.entities.Address;
import fr.trendev.kiam.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.utils.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author jsie
 */
@Entity
@Table(name = "COLLECTIVE_GROUP")
@IdClass(CollectiveGroupPK.class)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CollectiveGroup {

    @Id
    @Column(name = "COLLECTIVE_GROUP_ID")
    @NotNull(message = "CollectiveGroup ID cannot be null")
    private String id;

    @Basic
    private String groupName;

    @Basic
    private String phone;

    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    private Address address;

    @Id
    @ManyToOne
    @JoinColumn(name = "COLLECTIVE_GROUP_PRO_EMAIL", referencedColumnName = "EMAIL")
    @JsonIgnore
    private Professional professional;

    @OneToMany(mappedBy = "collectiveGroup", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CollectiveGroupBill> collectiveGroupBills = new LinkedList<>();

    public CollectiveGroup(String groupName, String phone, Professional professional) {
        this.groupName = groupName;
        this.phone = phone;
        this.professional = professional;
        this.address = new Address();
    }

    public CollectiveGroup(String groupName, String phone, Address address, Professional professional) {
        this.groupName = groupName;
        this.phone = phone;
        this.address = address;
        this.professional = professional;
    }

    public CollectiveGroup() {
        this.address = new Address();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public List<CollectiveGroupBill> getCollectiveGroupBills() {
        return collectiveGroupBills;
    }

    public void setCollectiveGroupBills(List<CollectiveGroupBill> collectiveGroupBills) {
        this.collectiveGroupBills = collectiveGroupBills;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}