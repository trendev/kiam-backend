/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.offering.controllers;

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
import fr.trendev.comptandye.offering.entities.OfferingPK;
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
import fr.trendev.comptandye.pack.controllers.PackFacade;
import fr.trendev.comptandye.sale.controllers.SaleFacade;
import fr.trendev.comptandye.service.controllers.ServiceFacade;
import fr.trendev.comptandye.utils.Visitor;
import java.util.Optional;
import java.util.function.Function;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
public class OfferingIntegrityVisitor implements Visitor<Offering> {

    private final PackFacade packFacade;

    private final ServiceFacade serviceFacade;

    private final SaleFacade saleFacade;

    private final String proEmail;

    public OfferingIntegrityVisitor(PackFacade packFacade,
            ServiceFacade serviceFacade, SaleFacade saleFacade, String proEmail) {
        this.packFacade = packFacade;
        this.serviceFacade = serviceFacade;
        this.saleFacade = saleFacade;
        this.proEmail = proEmail;
    }

    @Override
    public Offering visit(Address address) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Administrator administrator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Bill bill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Business business) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Category category) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Client client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(ClientBill clientBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(CollectiveGroup collectiveGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(CollectiveGroupBill collectiveGroupBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(CustomerDetails customerDetails) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Expense expense) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Individual individual) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(IndividualBill individualBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Offering offering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Pack pack) {
        return Optional.ofNullable(packFacade.find(new OfferingPK(pack.getId(),
                proEmail)))
                .map(p -> {
                    pack.getOfferings().forEach(o -> o.accept(this));
                    return p;
                })
                .orElseThrow(() -> new WebApplicationException("Pack ["
                        + pack.getId()
                        + "] not found !"));
    }

    @Override
    public Offering visit(Payment payment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(PaymentMode paymentMode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(PurchasedOffering purchasedOffering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Professional professional) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Sale sale) {
        return Optional.ofNullable(saleFacade.find(
                new OfferingPK(sale.getId(), proEmail)))
                .map(Function.identity())
                .orElseThrow(() -> new WebApplicationException("Sale ["
                        + sale.getId()
                        + "] not found !"));

    }

    @Override
    public Offering visit(Service service) {
        return Optional.ofNullable(serviceFacade.find(
                new OfferingPK(service.getId(), proEmail)))
                .map(Function.identity())
                .orElseThrow(() -> new WebApplicationException("Service ["
                        + service.getId()
                        + "] not found !"));

    }

    @Override
    public Offering visit(SocialNetworkAccounts socialNetworkAccounts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(UserAccount userAccount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(UserGroup userGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(ExpenseItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Product instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(ProductRecord instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(ProductReference instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(PurchaseExpense instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(PurchasedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(ReturnedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(SoldItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(UsedItem instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(Notification instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Offering visit(ThresholdAlert instance) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
