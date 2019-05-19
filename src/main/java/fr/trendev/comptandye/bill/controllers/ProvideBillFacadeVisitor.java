/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.bill.controllers;

import fr.trendev.comptandye.address.entities.Address;
import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.bill.entities.BillPK;
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
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.clientbill.controllers.ClientBillFacade;
import fr.trendev.comptandye.collectivegroupbill.controllers.CollectiveGroupBillFacade;
import fr.trendev.comptandye.individualbill.controllers.IndividualBillFacade;
import fr.trendev.comptandye.utils.Visitor;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class ProvideBillFacadeVisitor implements
        Visitor<AbstractFacade<? extends Bill, BillPK>> {

    @Inject
    private ClientBillFacade clientBillFacade;

    @Inject
    private CollectiveGroupBillFacade collectiveGroupBillFacade;

    @Inject
    private IndividualBillFacade individualBillFacade;

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Address address) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            Administrator administrator) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Bill bill) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            Business business) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            Category category) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Client client) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            ClientBill clientBill) {
        return clientBillFacade;
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            CollectiveGroup collectiveGroup) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            CollectiveGroupBill collectiveGroupBill) {
        return collectiveGroupBillFacade;
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            Customer customer) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            CustomerDetails customerDetails) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Expense expense) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            Individual individual) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            IndividualBill individualBill) {
        return individualBillFacade;
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            Offering offering) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Pack pack) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Payment payment) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            PaymentMode paymentMode) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            Professional professional) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Sale sale) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Service service) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            SocialNetworkAccounts socialNetworkAccounts) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            UserAccount userAccount) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            UserGroup userGroup) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            PurchasedOffering purchasedOffering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            ExpenseItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Product instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            ProductRecord instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            ProductReference instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            PurchaseExpense instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            PurchasedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            ReturnedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            SoldItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(
            UsedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(Notification instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Bill, BillPK> visit(ThresholdAlert instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}