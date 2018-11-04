/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.usergroup.controllers;

import fr.trendev.comptandye.address.entities.Address;
import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.category.entities.Category;
import fr.trendev.comptandye.client.entities.Client;
import fr.trendev.comptandye.clientbill.entities.ClientBill;
import fr.trendev.comptandye.collectivegroup.entities.CollectiveGroup;
import fr.trendev.comptandye.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.comptandye.usergroup.entities.Customer;
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
import fr.trendev.comptandye.purchaseditem.entities.PurchasedItem;
import fr.trendev.comptandye.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.comptandye.purchaseexpense.entities.PurchaseExpense;
import fr.trendev.comptandye.returneditem.entities.ReturnedItem;
import fr.trendev.comptandye.sale.entities.Sale;
import fr.trendev.comptandye.service.entities.Service;
import fr.trendev.comptandye.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.solditem.entities.SoldItem;
import fr.trendev.comptandye.thresholdalert.entities.ThresholdAlert;
import fr.trendev.comptandye.useditem.entities.UsedItem;
import fr.trendev.comptandye.useraccount.entities.UserAccount;
import fr.trendev.comptandye.useraccount.entities.UserAccountType;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import fr.trendev.comptandye.utils.Visitor;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class ProvideUserGroupVisitor implements Visitor<UserGroup> {

    @Inject
    UserGroupFacade userGroupFacade;

    private static final Logger LOG = Logger.getLogger(
            ProvideUserGroupVisitor.class.getName());

    @Override
    public UserGroup visit(Address address) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Administrator administrator) {
        return userGroupFacade.find(UserAccountType.ADMINISTRATOR);
    }

    @Override
    public UserGroup visit(Bill bill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Business business) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Category category) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Client client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(ClientBill clientBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(CollectiveGroup collectiveGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(CollectiveGroupBill collectiveGroupBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(CustomerDetails customerDetails) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Expense expense) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Individual individual) {
        return userGroupFacade.find(UserAccountType.INDIVIDUAL);
    }

    @Override
    public UserGroup visit(IndividualBill individualBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Offering offering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Pack pack) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Payment payment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(PaymentMode paymentMode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(PurchasedOffering purchasedOffering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Professional professional) {
        return userGroupFacade.find(UserAccountType.PROFESSIONAL);
    }

    @Override
    public UserGroup visit(Sale sale) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Service service) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(SocialNetworkAccounts socialNetworkAccounts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(UserAccount userAccount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(UserGroup userGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(ExpenseItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Product instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(ProductRecord instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(ProductReference instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(PurchaseExpense instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(PurchasedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(ReturnedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(SoldItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(UsedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(Notification instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public UserGroup visit(ThresholdAlert instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
