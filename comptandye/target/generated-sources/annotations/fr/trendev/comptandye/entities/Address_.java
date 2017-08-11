package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Customer;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Address.class)
public class Address_ { 

    public static volatile SingularAttribute<Address, String> country;
    public static volatile SingularAttribute<Address, String> city;
    public static volatile SingularAttribute<Address, String> street;
    public static volatile SingularAttribute<Address, String> postalCode;
    public static volatile SingularAttribute<Address, String> optional;
    public static volatile SingularAttribute<Address, Long> id;
    public static volatile ListAttribute<Address, Customer> customers;

}