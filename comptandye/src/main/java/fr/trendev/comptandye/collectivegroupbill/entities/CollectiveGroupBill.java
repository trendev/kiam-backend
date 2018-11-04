/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.collectivegroupbill.entities;

import fr.trendev.comptandye.collectivegroup.entities.CollectiveGroup;
import fr.trendev.comptandye.bill.entities.Bill;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.bill.entities.BillType;
import fr.trendev.comptandye.utils.visitors.Visitor;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CollectiveGroupBill extends Bill {

    @Basic
    private String recipient;

    @ManyToOne(targetEntity = CollectiveGroup.class)
    private CollectiveGroup collectiveGroup;

    public CollectiveGroupBill(String reference, Date deliveryDate, int amount,
            int discount, Date paymentDate, List comments,
            Professional professional, List payments, List purchasedOfferings,
            CollectiveGroup collectiveGroup) {
        super(reference, deliveryDate, amount, discount, paymentDate, comments,
                professional, payments, purchasedOfferings);
        this.collectiveGroup = collectiveGroup;
        this.cltype = BillType.COLLECTIVEGROUP;
    }

    public CollectiveGroupBill() {
        this.cltype = BillType.COLLECTIVEGROUP;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public CollectiveGroup getCollectiveGroup() {
        return this.collectiveGroup;
    }

    public void setCollectiveGroup(CollectiveGroup collectiveGroup) {
        this.collectiveGroup = collectiveGroup;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}