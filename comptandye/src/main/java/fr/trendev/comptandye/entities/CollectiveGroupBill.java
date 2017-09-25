/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.visitors.Visitor;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CollectiveGroupBill extends Bill {

    @ManyToOne(targetEntity = CollectiveGroup.class)
    private CollectiveGroup collectiveGroup;

    public CollectiveGroupBill(String reference, Date deliveryDate, int amount,
            int discount, Date paymentDate, List comments,
            Professional professional, List payments, List offerings,
            CollectiveGroup collectiveGroup) {
        super(reference, deliveryDate, amount, discount, paymentDate, comments,
                professional, payments, offerings);
        this.collectiveGroup = collectiveGroup;
    }

    public CollectiveGroupBill() {
    }

    public CollectiveGroup getCollectiveGroup() {
        return this.collectiveGroup;
    }

    public void setCollectiveGroup(CollectiveGroup collectiveGroup) {
        this.collectiveGroup = collectiveGroup;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);
    }

}