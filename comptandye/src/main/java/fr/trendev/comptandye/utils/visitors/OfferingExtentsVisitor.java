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
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.IndividualBill;
import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.OfferingExtents;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.entities.Sale;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.entities.UserGroup;

/**
 *
 * @author jsie This Visitor will analyze an Offering and compute its different
 * extents (Services/Sales)
 */
public class OfferingExtentsVisitor implements Visitor<OfferingExtents> {

    @Override
    public OfferingExtents visit(Address address) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Administrator administrator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Bill bill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Business business) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Category category) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Client client) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(ClientBill clientBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(CollectiveGroup collectiveGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(CollectiveGroupBill collectiveGroupBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Customer customer) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(CustomerDetails customerDetails) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Expense expense) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Individual individual) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(IndividualBill individualBill) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Offering offering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Pack pack) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Payment payment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(PaymentMode paymentMode) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(PurchasedOffering purchasedOffering) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Professional professional) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Sale sale) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(Service service) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(SocialNetworkAccounts socialNetworkAccounts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(UserAccount userAccount) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public OfferingExtents visit(UserGroup userGroup) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
