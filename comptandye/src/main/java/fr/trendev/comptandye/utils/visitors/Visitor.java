/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.visitors;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Bill;
import fr.trendev.comptandye.entities.Business;
import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.CollectiveGroupBill;
import fr.trendev.comptandye.entities.Customer;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.ExpenseItem;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.IndividualBill;
import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductRecord;
import fr.trendev.comptandye.entities.ProductReference;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Purchase;
import fr.trendev.comptandye.entities.PurchasedItem;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.entities.ReturnedItem;
import fr.trendev.comptandye.entities.Sale;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.SoldItem;
import fr.trendev.comptandye.entities.UsedItem;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.entities.UserGroup;

/**
 *
 * @author jsie
 */
public interface Visitor<T> {

    public T visit(Address instance);

    public T visit(Administrator instance);

    public T visit(Bill instance);

    public T visit(Business instance);

    public T visit(Category instance);

    public T visit(Client instance);

    public T visit(ClientBill instance);

    public T visit(CollectiveGroup instance);

    public T visit(CollectiveGroupBill instance);

    public T visit(Customer instance);

    public T visit(CustomerDetails instance);

    public T visit(Expense instance);

    public T visit(ExpenseItem instance);

    public T visit(Individual instance);

    public T visit(IndividualBill instance);

    public T visit(Offering instance);

    public T visit(Pack instance);

    public T visit(Payment instance);

    public T visit(PaymentMode instance);

    public T visit(Product instance);

    public T visit(ProductRecord instance);

    public T visit(ProductReference instance);

    public T visit(Professional instance);

    public T visit(Purchase instance);

    public T visit(PurchasedItem instance);

    public T visit(PurchasedOffering instance);

    public T visit(ReturnedItem instance);

    public T visit(Sale instance);

    public T visit(Service instance);

    public T visit(SoldItem instance);

    public T visit(SocialNetworkAccounts instance);

    public T visit(UserAccount instance);

    public T visit(UsedItem instance);

    public T visit(UserGroup instance);
}
