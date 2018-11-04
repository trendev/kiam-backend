/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.clientbill.entities;

import fr.trendev.comptandye.client.entities.Client;
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
public class ClientBill extends Bill {

    @ManyToOne(targetEntity = Client.class)
    private Client client;

    public ClientBill(String reference, Date deliveryDate, int amount,
            int discount, Date paymentDate, List comments,
            Professional professional, List payments, List purchasedOfferings,
            Client client) {
        super(reference, deliveryDate, amount, discount, paymentDate, comments,
                professional, payments, purchasedOfferings);
        this.client = client;
        this.cltype = BillType.CLIENT;
    }

    public ClientBill() {
        this.cltype = BillType.CLIENT;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}