package fr.trendev.comptandye.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(CustomerDetails.class)
public class CustomerDetails_ { 

    public static volatile SingularAttribute<CustomerDetails, String> picturePath;
    public static volatile SingularAttribute<CustomerDetails, String> firstName;
    public static volatile SingularAttribute<CustomerDetails, String> lastName;
    public static volatile SingularAttribute<CustomerDetails, Date> birthdate;
    public static volatile ListAttribute<CustomerDetails, String> comments;
    public static volatile SingularAttribute<CustomerDetails, String> phone;
    public static volatile SingularAttribute<CustomerDetails, Character> sex;
    public static volatile SingularAttribute<CustomerDetails, String> nickname;
    public static volatile SingularAttribute<CustomerDetails, Long> id;

}