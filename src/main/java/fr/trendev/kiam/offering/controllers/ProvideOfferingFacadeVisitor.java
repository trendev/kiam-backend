/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.offering.controllers;

import fr.trendev.kiam.address.entities.Address;
import fr.trendev.kiam.administrator.entities.Administrator;
import fr.trendev.kiam.bill.entities.Bill;
import fr.trendev.kiam.business.entities.Business;
import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.clientbill.entities.ClientBill;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroup;
import fr.trendev.kiam.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.customerdetails.entities.CustomerDetails;
import fr.trendev.kiam.expense.entities.Expense;
import fr.trendev.kiam.expenseitem.entities.ExpenseItem;
import fr.trendev.kiam.individual.entities.Individual;
import fr.trendev.kiam.individualbill.entities.IndividualBill;
import fr.trendev.kiam.notification.entities.Notification;
import fr.trendev.kiam.offering.entities.Offering;
import fr.trendev.kiam.offering.entities.OfferingPK;
import fr.trendev.kiam.pack.controllers.PackFacade;
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
import fr.trendev.kiam.sale.controllers.SaleFacade;
import fr.trendev.kiam.sale.entities.Sale;
import fr.trendev.kiam.service.controllers.ServiceFacade;
import fr.trendev.kiam.service.entities.Service;
import fr.trendev.kiam.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.kiam.solditem.entities.SoldItem;
import fr.trendev.kiam.thresholdalert.entities.ThresholdAlert;
import fr.trendev.kiam.useditem.entities.UsedItem;
import fr.trendev.kiam.useraccount.entities.Customer;
import fr.trendev.kiam.useraccount.entities.UserAccount;
import fr.trendev.kiam.usergroup.entities.UserGroup;
import fr.trendev.kiam.utils.Visitor;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 *
 * @author jsie
 */
@Singleton
public class ProvideOfferingFacadeVisitor implements
        Visitor<AbstractFacade<? extends Offering, OfferingPK>> {

    @Inject
    ServiceFacade serviceFacade;

    @Inject
    PackFacade packFacade;

    @Inject
    SaleFacade saleFacade;

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Address address) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Administrator administrator) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Bill bill) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Business business) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Category category) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Client client) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            ClientBill clientBill) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            CollectiveGroup collectiveGroup) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            CollectiveGroupBill collectiveGroupBill) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Customer customer) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            CustomerDetails customerDetails) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Expense expense) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Individual individual) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            IndividualBill individualBill) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Offering offering) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Pack pack) {
        return packFacade;
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Payment payment) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            PaymentMode paymentMode) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Professional professional) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Sale sale) {
        return saleFacade;
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Service service) {
        return serviceFacade;
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            SocialNetworkAccounts socialNetworkAccounts) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            UserAccount userAccount) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            UserGroup userGroup) {
        throw new UnsupportedOperationException("Not supported !");
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            PurchasedOffering purchasedOffering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            ExpenseItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(Product instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            ProductRecord instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            ProductReference instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            PurchaseExpense instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            PurchasedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            ReturnedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            SoldItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            UsedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            Notification instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AbstractFacade<? extends Offering, OfferingPK> visit(
            ThresholdAlert instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
