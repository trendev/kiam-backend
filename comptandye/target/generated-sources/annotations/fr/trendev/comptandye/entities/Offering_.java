package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Professional;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Offering.class)
public abstract class Offering_ { 

    public static volatile SingularAttribute<Offering, Integer> duration;
    public static volatile SingularAttribute<Offering, Float> price;
    public static volatile SingularAttribute<Offering, String> name;
    public static volatile SingularAttribute<Offering, Long> id;
    public static volatile SingularAttribute<Offering, Professional> professional;

}