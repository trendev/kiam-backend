/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.address.entities.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.utils.UserAccountType;
import fr.trendev.comptandye.utils.visitors.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Individual extends Customer {

    @OneToMany(cascade = {CascadeType.ALL}, targetEntity = IndividualBill.class,
            mappedBy = "individual")
    @JsonIgnore
    private List<IndividualBill> individualBills = new LinkedList<>();

    @ManyToMany(targetEntity = Professional.class)
    @JsonIgnore
    private List<Professional> professionals = new LinkedList<>();

    public Individual(String email, String password, String username,
            String uuid) {
        super(email, password, username, uuid);
        this.cltype = UserAccountType.INDIVIDUAL;
    }

    public Individual() {
        super();
        this.cltype = UserAccountType.INDIVIDUAL;
    }

    public Individual(String email, String password, String username,
            String uuid, CustomerDetails customerDetails, Address address,
            SocialNetworkAccounts socialNetworkAccounts) {
        super(email, password, username, uuid, customerDetails, address,
                socialNetworkAccounts);
        this.cltype = UserAccountType.INDIVIDUAL;
    }

    public List<IndividualBill> getIndividualBills() {
        return this.individualBills;
    }

    public void setIndividualBills(List<IndividualBill> individualBills) {
        this.individualBills = individualBills;
    }

    public List<Professional> getProfessionals() {
        return this.professionals;
    }

    public void setProfessionals(List<Professional> professionals) {
        this.professionals = professionals;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}