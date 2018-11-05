/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.individualbill.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.bill.entities.BillType;
import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.utils.Visitor;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class IndividualBill extends Bill {

    @ManyToOne(targetEntity = Individual.class)
    private Individual individual;

    public IndividualBill() {
        this.cltype = BillType.INDIVIDUAL;
    }

    public Individual getIndividual() {
        return this.individual;
    }

    public void setIndividual(Individual individual) {
        this.individual = individual;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}