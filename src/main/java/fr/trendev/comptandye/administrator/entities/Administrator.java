/** * This file was generated by the JPA Modeler */
package fr.trendev.comptandye.administrator.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import fr.trendev.comptandye.useraccount.entities.UserAccount;
import fr.trendev.comptandye.useraccount.entities.UserAccountType;
import fr.trendev.comptandye.utils.Visitor;
import javax.persistence.Entity;

/**
 * @author jsie
 */
@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Administrator extends UserAccount {

    public Administrator(String email, String password, String username, String uuid) {
        super(email, password, username, uuid);
        this.cltype = UserAccountType.ADMINISTRATOR;
    }

    public Administrator() {
        super();
        this.cltype = UserAccountType.ADMINISTRATOR;
    }

    @Override
    public <T> T accept(Visitor<T> v) {
        return v.visit(this);
    }

}