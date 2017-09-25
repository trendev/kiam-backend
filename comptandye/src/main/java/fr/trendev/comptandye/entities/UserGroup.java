/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.visitors.Visitor;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * User groups should be linked to User Accounts type but it's still possible to
 * create many usergroup. This class is linked to jdbc-realm authentication.
 *
 * @author jsie
 */
@Entity
@Table(name = "USER_GROUP")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserGroup {

    @Id
    private String name;

    @Basic
    private String description;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST,
        CascadeType.REFRESH}, targetEntity = UserAccount.class,
            mappedBy = "userGroups")
    @JsonIgnore
    private List<UserAccount> userAccounts = new LinkedList<>();

    public UserGroup(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public UserGroup() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserAccount> getUserAccounts() {
        return this.userAccounts;
    }

    public void setUserAccounts(List<UserAccount> userAccounts) {
        this.userAccounts = userAccounts;
    }

    public void accept(Visitor v) {
        v.visit(this);
    }

}