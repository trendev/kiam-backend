/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.returneditem.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.productrecord.entities.ProductRecord;
import fr.trendev.comptandye.productrecord.entities.ProductRecordType;
import fr.trendev.comptandye.utils.Visitor;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

/**
 * Recorded add when a Bill is cancelled and the product are returned
 *
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReturnedItem extends ProductRecord {

    @OneToOne(targetEntity = Bill.class)
    @JoinTable(name = "RETURNED_ITEM", joinColumns = {
        @JoinColumn(name = "ID", referencedColumnName = "ID",
                table = "PRODUCTRECORD")})
    private Bill cancelledBill;

    public ReturnedItem() {
        super();
        this.cltype = ProductRecordType.RETURNED_ITEM;
    }

    public Bill getCancelledBill() {
        return this.cancelledBill;
    }

    public void setCancelledBill(Bill cancelledBill) {
        this.cancelledBill = cancelledBill;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}