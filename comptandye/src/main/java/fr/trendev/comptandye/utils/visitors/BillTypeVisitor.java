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
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.entities.UserGroup;
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

    public static final String INDIVIDUAL_CLTYPE = "individualbill";
    public static final String CLIENT_CLTYPE = "clientbill";
    public static final String COLLECTIVEGROUP_CLTYPE = "collectivegroupbill";

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

}
