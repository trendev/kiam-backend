/**
 * This file was generated by the JPA Modeler
 */ 

package fr.trendev.comptandye.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

/**
 * @author jsie
 */

@Entity
public class ClientBill extends Bill { 

    @OneToOne(targetEntity = CollectiveGroup.class)
    private CollectiveGroup collectiveGroup;

    @ManyToOne(targetEntity = Client.class)
    private Client client;


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