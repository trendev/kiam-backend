/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.bill.controllers;

import fr.trendev.kiam.address.entities.Address;
import fr.trendev.kiam.administrator.entities.Administrator;
import fr.trendev.kiam.bill.entities.Bill;
import fr.trendev.kiam.business.entities.Business;
import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.clientbill.entities.ClientBill;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroup;
import fr.trendev.kiam.collectivegroupbill.entities.CollectiveGroupBill;
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
import fr.trendev.kiam.purchaseditem.entities.PurchasedItem;
import fr.trendev.kiam.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.kiam.purchaseexpense.entities.PurchaseExpense;
import fr.trendev.kiam.returneditem.entities.ReturnedItem;
import fr.trendev.kiam.sale.entities.Sale;
import fr.trendev.kiam.service.entities.Service;
import fr.trendev.kiam.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.kiam.solditem.entities.SoldItem;
import fr.trendev.kiam.thresholdalert.entities.ThresholdAlert;
import fr.trendev.kiam.useditem.entities.UsedItem;
import fr.trendev.kiam.useraccount.entities.Customer;
import fr.trendev.kiam.useraccount.entities.UserAccount;
import fr.trendev.kiam.usergroup.entities.UserGroup;
import fr.trendev.kiam.utils.Visitor;
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
