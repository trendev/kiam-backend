package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.Professional;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(CollectiveGroup.class)
public class CollectiveGroup_ { 

    public static volatile SingularAttribute<CollectiveGroup, String> groupName;
    public static volatile SingularAttribute<CollectiveGroup, Address> address;
    public static volatile ListAttribute<CollectiveGroup, Client> clients;
    public static volatile SingularAttribute<CollectiveGroup, Long> id;
    public static volatile SingularAttribute<CollectiveGroup, Professional> professional;

}