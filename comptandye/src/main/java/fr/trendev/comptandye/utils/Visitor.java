/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.address.entities.Address;
import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.category.entities.Category;
import fr.trendev.comptandye.client.entities.Client;
import fr.trendev.comptandye.clientbill.entities.ClientBill;
import fr.trendev.comptandye.collectivegroup.entities.CollectiveGroup;
import fr.trendev.comptandye.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.comptandye.useraccount.entities.Customer;
import fr.trendev.comptandye.customerdetails.entities.CustomerDetails;
import fr.trendev.comptandye.expense.entities.Expense;
import fr.trendev.comptandye.expenseitem.entities.ExpenseItem;
import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.individualbill.entities.IndividualBill;
import fr.trendev.comptandye.notification.entities.Notification;
import fr.trendev.comptandye.offering.entities.Offering;
import fr.trendev.comptandye.pack.entities.Pack;
import fr.trendev.comptandye.payment.entities.Payment;
import fr.trendev.comptandye.paymentmode.entities.PaymentMode;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.productrecord.entities.ProductRecord;
import fr.trendev.comptandye.productreference.entities.ProductReference;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.purchaseexpense.entities.PurchaseExpense;
import fr.trendev.comptandye.purchaseditem.entities.PurchasedItem;
import fr.trendev.comptandye.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.comptandye.returneditem.entities.ReturnedItem;
import fr.trendev.comptandye.sale.entities.Sale;
import fr.trendev.comptandye.service.entities.Service;
import fr.trendev.comptandye.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.solditem.entities.SoldItem;
import fr.trendev.comptandye.thresholdalert.entities.ThresholdAlert;
import fr.trendev.comptandye.useditem.entities.UsedItem;
import fr.trendev.comptandye.useraccount.entities.UserAccount;
import fr.trendev.comptandye.usergroup.entities.UserGroup;

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
