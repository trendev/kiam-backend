package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Customer.class)
public abstract class Customer_ extends UserAccount_ {

    public static volatile SingularAttribute<Customer, Address> address;
    public static volatile SingularAttribute<Customer, SocialNetworkAccounts> socialNetworkAccounts;
    public static volatile SingularAttribute<Customer, CustomerDetails> customerDetails;

}