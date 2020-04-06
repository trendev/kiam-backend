/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.utils;

import fr.trendev.kiam.address.entities.Address;
import fr.trendev.kiam.administrator.entities.Administrator;
import fr.trendev.kiam.bill.entities.Bill;
import fr.trendev.kiam.business.entities.Business;
import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.clientbill.entities.ClientBill;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroup;
import fr.trendev.kiam.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.kiam.useraccount.entities.Customer;
import fr.trendev.kiam.customerdetails.entities.CustomerDetails;
import fr.trendev.kiam.expense.entities.Expense;
import fr.trendev.kiam.expenseitem.entities.ExpenseItem;
import fr.trendev.kiam.individual.entities.Individual;
import fr.trendev.kiam.individualbill.entities.IndividualBill;
import fr.trendev.kiam.notification.entities.Notification;
import fr.trendev.kiam.offering.entities.Offering;
import fr.trendev.kiam.pack.entities.Pack;
import fr.trendev.kiam.payment.entities.Payment;
import fr.trendev.kiam.paymentmode.entities.PaymentMode;
import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.productrecord.entities.ProductRecord;
import fr.trendev.kiam.productreference.entities.ProductReference;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.purchaseexpense.entities.PurchaseExpense;
import fr.trendev.kiam.purchaseditem.entities.PurchasedItem;
import fr.trendev.kiam.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.kiam.returneditem.entities.ReturnedItem;
import fr.trendev.kiam.sale.entities.Sale;
import fr.trendev.kiam.service.entities.Service;
import fr.trendev.kiam.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.kiam.solditem.entities.SoldItem;
import fr.trendev.kiam.thresholdalert.entities.ThresholdAlert;
import fr.trendev.kiam.useditem.entities.UsedItem;
import fr.trendev.kiam.useraccount.entities.UserAccount;
import fr.trendev.kiam.usergroup.entities.UserGroup;

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

    public T visit(Notification instance);

    public T visit(Offering instance);

    public T visit(Pack instance);

    public T visit(Payment instance);

    public T visit(PaymentMode instance);

    public T visit(Product instance);

    public T visit(ProductRecord instance);

    public T visit(ProductReference instance);

    public T visit(Professional instance);

    public T visit(PurchaseExpense instance);

    public T visit(PurchasedItem instance);

    public T visit(PurchasedOffering instance);

    public T visit(ReturnedItem instance);

    public T visit(Sale instance);

    public T visit(Service instance);

    public T visit(SoldItem instance);

    public T visit(SocialNetworkAccounts instance);

    public T visit(ThresholdAlert instance);

    public T visit(UserAccount instance);

    public T visit(UsedItem instance);

    public T visit(UserGroup instance);
}
