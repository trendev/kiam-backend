/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.bill.controllers;

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
import fr.trendev.comptandye.utils.Visitor;
import javax.inject.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class BillTypeVisitor implements Visitor<String> {

    public static final String INDIVIDUAL_PREFIX = "IX";
    public static final String CLIENT_PREFIX = "CX";
    public static final String COLLECTIVEGROUP_PREFIX = "CG";

    @Override
    public String visit(Address address) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Administrator administrator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Bill bill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Business business) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Category category) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Client client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(ClientBill clientBill) {
        return CLIENT_PREFIX;
    }

    @Override
    public String visit(CollectiveGroup collectiveGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(CollectiveGroupBill collectiveGroupBill) {
        return COLLECTIVEGROUP_PREFIX;
    }

    @Override
    public String visit(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(CustomerDetails customerDetails) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Expense expense) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Individual individual) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(IndividualBill individualBill) {
        return INDIVIDUAL_PREFIX;
    }

    @Override
    public String visit(Offering offering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Pack pack) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Payment payment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(PaymentMode paymentMode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Professional professional) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Sale sale) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Service service) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(SocialNetworkAccounts socialNetworkAccounts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(UserAccount userAccount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(UserGroup userGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(PurchasedOffering purchasedOffering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(ExpenseItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Product instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(ProductRecord instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(ProductReference instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(PurchaseExpense instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(PurchasedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(ReturnedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(SoldItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(UsedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(Notification instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String visit(ThresholdAlert instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
