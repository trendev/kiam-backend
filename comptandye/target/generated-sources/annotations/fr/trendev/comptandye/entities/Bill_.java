package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.Professional;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Bill.class)
public abstract class Bill_ { 

    public static volatile SingularAttribute<Bill, Long> reference;
    public static volatile SingularAttribute<Bill, Float> amount;
    public static volatile ListAttribute<Bill, Payment> payments;
    public static volatile SingularAttribute<Bill, Float> discount;
    public static volatile SingularAttribute<Bill, String> comment;
    public static volatile SingularAttribute<Bill, Date> deliveryDate;
    public static volatile SingularAttribute<Bill, Date> paymentDate;
    public static volatile ListAttribute<Bill, Offering> offerings;
    public static volatile SingularAttribute<Bill, Professional> professional;

}