/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientBill extends Bill {

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = CollectiveGroup.class)
    @JsonIgnore
    private CollectiveGroup collectiveGroup;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = Client.class)
    @JsonIgnore
    private Client client;

    public ClientBill(String reference, Date deliveryDate, int amount,
            int discount, Date paymentDate, List comments,
            Professional professional, List payments, List offerings,
            CollectiveGroup collectiveGroup, Client client) {
        super(reference, deliveryDate, amount, discount, paymentDate, comments,
                professional, payments, offerings);
        this.collectiveGroup = collectiveGroup;
        this.client = client;
    }

    public ClientBill() {
    }

    public CollectiveGroup getCollectiveGroup() {
        return this.collectiveGroup;
    }

    public void setCollectiveGroup(CollectiveGroup collectiveGroup) {
        this.collectiveGroup = collectiveGroup;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

}