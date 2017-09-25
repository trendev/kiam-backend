/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.visitors.Visitor;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author jsie
 */
@Entity
@Table(name = "SOCIAL_NETWORK_ACCOUNT")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SocialNetworkAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String facebook;

    @Basic
    private String twitter;

    @Basic
    private String instagram;

    @Basic
    private String pinterest;

    public SocialNetworkAccounts(String facebook, String twitter,
            String instagram, String pinterest) {
        this.facebook = facebook;
        this.twitter = twitter;
        this.instagram = instagram;
        this.pinterest = pinterest;
    }

    public SocialNetworkAccounts() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacebook() {
        return this.facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return this.twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getInstagram() {
        return this.instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getPinterest() {
        return this.pinterest;
    }

    public void setPinterest(String pinterest) {
        this.pinterest = pinterest;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}