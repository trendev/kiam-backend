package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Bill;
import fr.trendev.comptandye.entities.PaymentMode;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Payment.class)
public class Payment_ { 

    public static volatile SingularAttribute<Payment, Float> amount;
    public static volatile SingularAttribute<Payment, PaymentMode> paymentMode;
    public static volatile SingularAttribute<Payment, Bill> bill;
    public static volatile SingularAttribute<Payment, String> currency;
    public static volatile SingularAttribute<Payment, Long> id;

}