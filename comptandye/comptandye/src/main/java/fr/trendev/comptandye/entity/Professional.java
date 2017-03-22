/**
 * This file was generated by the JPA Modeler
 */ 

package fr.trendev.comptandye.entity;

import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 * @author jsie
 */

@Entity
public class Professional extends Customer { 

    @Basic
    private String website;

    @OneToOne(targetEntity = SocialNetworkAccount.class)
    private SocialNetworkAccount socialNetworkAccount;

    @OneToMany(targetEntity = Offering.class,mappedBy = "professional")
    private List<Offering> offerings;

    @OneToMany(targetEntity = Client.class,mappedBy = "professional")
    private List<Client> clients;

    @ManyToMany(targetEntity = Individual.class)
    @JoinTable(name="CLIENT_RELATIONSHIP")
    private List<Individual> individuals;


    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public SocialNetworkAccount getSocialNetworkAccount() {
        return this.socialNetworkAccount;
    }

    public void setSocialNetworkAccount(SocialNetworkAccount socialNetworkAccount) {
        this.socialNetworkAccount = socialNetworkAccount;
    }

    public List<Offering> getOfferings() {
        return this.offerings;
    }

    public void setOfferings(List<Offering> offerings) {
        this.offerings = offerings;
    }

    public List<Client> getClients() {
        return this.clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Individual> getIndividuals() {
        return this.individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }


}
