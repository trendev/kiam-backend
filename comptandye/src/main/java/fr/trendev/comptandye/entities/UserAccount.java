/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author jsie
 */
@Entity
@Table(name = "USER_ACCOUNT")
@DiscriminatorColumn(length = 31, name = "ACCOUNT_TYPE")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public abstract class UserAccount {

    @Id
    private String email;

    @Basic
    private String password;

    @Basic
    private String username;

    @Basic
    private String uuid;

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = UserGroup.class)
    @JsonIgnore
    private List<UserGroup> userGroups;

    public UserAccount(String email, String password, String username,
            String uuid) {
        this();
        this.email = email;
        this.password = password;
        this.username = username;
        this.uuid = uuid;
    }

    public UserAccount() {
        this.registrationDate = new Date();
        this.userGroups = new LinkedList<>();
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getRegistrationDate() {
        return this.registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public List<UserGroup> getUserGroups() {
        return this.userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

}