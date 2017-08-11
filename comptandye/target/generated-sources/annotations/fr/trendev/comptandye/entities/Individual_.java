package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.InvidualBill;
import fr.trendev.comptandye.entities.Professional;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Individual.class)
public class Individual_ extends Customer_ {

    public static volatile ListAttribute<Individual, InvidualBill> invidualBills;
    public static volatile ListAttribute<Individual, Professional> professionals;

}