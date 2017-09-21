/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
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
            Professional professional, List payments, List offerings,
            Client client) {
        super(reference, deliveryDate, amount, discount, paymentDate, comments,
                professional, payments, offerings);
        this.client = client;
    }

    public ClientBill() {
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}