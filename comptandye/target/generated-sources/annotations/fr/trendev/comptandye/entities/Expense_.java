package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Expense.class)
public class Expense_ { 

    public static volatile SingularAttribute<Expense, Float> amount;
    public static volatile SingularAttribute<Expense, String> invoiceRef;
    public static volatile SingularAttribute<Expense, PaymentMode> paymentMode;
    public static volatile SingularAttribute<Expense, String> name;
    public static volatile SingularAttribute<Expense, Long> id;
    public static volatile ListAttribute<Expense, String> categories;
    public static volatile SingularAttribute<Expense, Date> paymentDate;
    public static volatile SingularAttribute<Expense, Professional> professional;

}