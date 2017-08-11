package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.UserGroup;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(UserAccount.class)
public abstract class UserAccount_ { 

    public static volatile ListAttribute<UserAccount, UserGroup> userGroups;
    public static volatile SingularAttribute<UserAccount, String> password;
    public static volatile SingularAttribute<UserAccount, Date> registrationDate;
    public static volatile SingularAttribute<UserAccount, String> uuid;
    public static volatile SingularAttribute<UserAccount, String> email;
    public static volatile SingularAttribute<UserAccount, String> username;

}