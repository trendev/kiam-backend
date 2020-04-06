/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.individual.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.individualbill.entities.IndividualBill;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.useraccount.entities.Customer;
import fr.trendev.kiam.useraccount.entities.UserAccountType;
import fr.trendev.kiam.utils.Visitor;
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

    @OneToMany(mappedBy = "individual", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<IndividualBill> individualBills = new LinkedList<>();

    @ManyToMany
    @JsonIgnore
    private List<Professional> professionals = new LinkedList<>();

    public Individual() {
        super();
        this.cltype = UserAccountType.INDIVIDUAL;
    }

    public List<IndividualBill> getIndividualBills() {
        return individualBills;
    }

    public void setIndividualBills(List<IndividualBill> individualBills) {
        this.individualBills = individualBills;
    }

    public List<Professional> getProfessionals() {
        return professionals;
    }

    public void setProfessionals(List<Professional> professionals) {
        this.professionals = professionals;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}