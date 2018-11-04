/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.individualbill.entities;

import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.bill.entities.Bill;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.bill.entities.BillType;
import fr.trendev.comptandye.utils.visitors.Visitor;
import java.util.Date;
import java.util.List;
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

    public IndividualBill(String reference, Date deliveryDate, int amount,
            int discount, Date paymentDate, List comments,
            Professional professional, List payments, List purchasedOfferings,
            Individual individual) {
        super(reference, deliveryDate, amount, discount, paymentDate, comments,
                professional, payments, purchasedOfferings);
        this.individual = individual;
        this.cltype = BillType.INDIVIDUAL;
    }

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