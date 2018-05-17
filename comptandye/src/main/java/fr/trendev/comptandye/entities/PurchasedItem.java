/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.utils.ProductRecordType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Recored added when an item is purchased(bought by the professional) must be
 * linked to an Expense (Purchase)
 *
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PurchasedItem extends ProductRecord {

    @ManyToOne(targetEntity = Purchase.class)
    private Purchase purchase;

    public PurchasedItem() {
        super();
        this.cltype = ProductRecordType.PURCHASED_ITEM;
    }

    public Purchase getPurchase() {
        return this.purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

}