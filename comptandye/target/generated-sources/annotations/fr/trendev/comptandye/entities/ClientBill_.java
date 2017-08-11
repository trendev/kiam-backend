package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.CollectiveGroup;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(ClientBill.class)
public class ClientBill_ extends Bill_ {

    public static volatile SingularAttribute<ClientBill, CollectiveGroup> collectiveGroup;
    public static volatile SingularAttribute<ClientBill, Client> client;

}