package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Client.class)
public class Client_ { 

    public static volatile SingularAttribute<Client, Address> address;
    public static volatile ListAttribute<Client, CollectiveGroup> collectiveGroups;
    public static volatile SingularAttribute<Client, SocialNetworkAccounts> socialNetworkAccounts;
    public static volatile ListAttribute<Client, ClientBill> clientBills;
    public static volatile SingularAttribute<Client, Long> id;
    public static volatile SingularAttribute<Client, CustomerDetails> customerDetails;
    public static volatile SingularAttribute<Client, Category> category;
    public static volatile SingularAttribute<Client, String> email;
    public static volatile SingularAttribute<Client, Professional> professional;

}