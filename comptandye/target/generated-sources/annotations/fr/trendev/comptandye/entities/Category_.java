package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.Professional;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Category.class)
public class Category_ { 

    public static volatile ListAttribute<Category, Client> clients;
    public static volatile SingularAttribute<Category, String> name;
    public static volatile SingularAttribute<Category, String> description;
    public static volatile SingularAttribute<Category, Long> id;
    public static volatile SingularAttribute<Category, Professional> professional;

}