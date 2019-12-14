/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.useraccount.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import fr.trendev.comptandye.utils.Visitor;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * An UserAccount can be an Administrator, a Professional or an Individual. This
 * class is linked to the jdbc-realm authentication.
 *
 * @author jsie
 */
@Entity
@Table(name = "USER_ACCOUNT")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "cltype", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Administrator.class, name = UserAccountType.ADMINISTRATOR),
    @JsonSubTypes.Type(value = Individual.class, name = UserAccountType.INDIVIDUAL),
    @JsonSubTypes.Type(value = Professional.class, name = UserAccountType.PROFESSIONAL)})
public abstract class UserAccount {

    @Id
    private String email;

    @Basic
    @NotNull(message = "cltype field in UserAccount cannot be null")
    protected String cltype;

    @Basic
    private String password;

    @Basic
    private String username;

    @Basic
    private String uuid;

    @Basic
    @Column(columnDefinition = "DATETIME(3)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate = new Date();

    @Basic
    private boolean blocked = true;

    @Basic
    @JsonIgnore
    private long lastAccessedTime = System.currentTimeMillis();

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<UserGroup> userGroups = new LinkedList<>();

    public UserAccount(String email, String password, String username, String uuid) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.uuid = uuid;
    }

    public UserAccount() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCltype() {
        return cltype;
    }

    public void setCltype(String cltype) {
        this.cltype = cltype;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public long getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public List<UserGroup> getUserGroups() {
        return userGroups;
    }

    public void setUserGroups(List<UserGroup> userGroups) {
        this.userGroups = userGroups;
    }

    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}