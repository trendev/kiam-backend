package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.entities.Bill;
import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.PaymentMode;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.6.4.v20160829-rNA", date="2017-08-07T17:33:29")
@StaticMetamodel(Professional.class)
public class Professional_ extends Customer_ {

    public static volatile SingularAttribute<Professional, String> website;
    public static volatile SingularAttribute<Professional, String> companyID;
    public static volatile ListAttribute<Professional, Client> clients;
    public static volatile SingularAttribute<Professional, String> business;
    public static volatile ListAttribute<Professional, CollectiveGroup> collectiveGroups;
    public static volatile ListAttribute<Professional, Bill> bills;
    public static volatile ListAttribute<Professional, Category> categories;
    public static volatile ListAttribute<Professional, PaymentMode> paymentModes;
    public static volatile ListAttribute<Professional, Individual> individuals;
    public static volatile ListAttribute<Professional, Offering> offerings;
    public static volatile ListAttribute<Professional, Expense> expenses;

}