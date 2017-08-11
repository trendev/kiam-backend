package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Customer;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(SocialNetworkAccounts.class)
public class SocialNetworkAccounts_ { 

    public static volatile SingularAttribute<SocialNetworkAccounts, String> twitter;
    public static volatile SingularAttribute<SocialNetworkAccounts, String> facebook;
    public static volatile SingularAttribute<SocialNetworkAccounts, String> pinterest;
    public static volatile SingularAttribute<SocialNetworkAccounts, Long> id;
    public static volatile SingularAttribute<SocialNetworkAccounts, String> instagram;
    public static volatile SingularAttribute<SocialNetworkAccounts, Customer> customer;

}