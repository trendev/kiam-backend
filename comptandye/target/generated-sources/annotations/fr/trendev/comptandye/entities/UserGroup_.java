package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.UserAccount;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(UserGroup.class)
public class UserGroup_ { 

    public static volatile SingularAttribute<UserGroup, String> name;
    public static volatile SingularAttribute<UserGroup, String> description;
    public static volatile ListAttribute<UserGroup, UserAccount> userAccounts;

}