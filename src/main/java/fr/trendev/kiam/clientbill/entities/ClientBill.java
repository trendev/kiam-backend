/** * This file was generated by the JPA Modeler */
package fr.trendev.kiam.clientbill.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.kiam.bill.entities.Bill;
import fr.trendev.kiam.bill.entities.BillType;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.utils.Visitor;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientBill extends Bill {

    @ManyToOne
    private Client client;

    public ClientBill() {
        this.cltype = BillType.CLIENT;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}