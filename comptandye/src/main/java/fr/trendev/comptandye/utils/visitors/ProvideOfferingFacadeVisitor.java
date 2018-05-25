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
import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductRecord;
import fr.trendev.comptandye.entities.ProductReference;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.PurchaseExpense;
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
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.PackFacade;
import fr.trendev.comptandye.sessions.SaleFacade;
import fr.trendev.comptandye.sessions.ServiceFacade;
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

}
