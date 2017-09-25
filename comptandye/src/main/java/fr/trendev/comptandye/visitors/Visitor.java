/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.visitors;

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
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.entities.UserGroup;

/**
 *
 * @author jsie
 */
public interface Visitor {

    public void visit(Address address);

    public void visit(Administrator administrator);

    public void visit(Bill bill);

    public void visit(Business business);

    public void visit(Category category);

    public void visit(Client client);

    public void visit(ClientBill clientBill);

    public void visit(CollectiveGroup collectiveGroup);

    public void visit(CollectiveGroupBill collectiveGroupBill);

    public void visit(Customer customer);

    public void visit(CustomerDetails customerDetails);

    public void visit(Expense expense);

    public void visit(Individual individual);

    public void visit(IndividualBill individualBill);

    public void visit(Offering offering);

    public void visit(Pack pack);

    public void visit(Payment payment);

    public void visit(PaymentMode paymentMode);

    public void visit(Professional professional);

    public void visit(Service service);

    public void visit(SocialNetworkAccounts socialNetworkAccounts);

    public void visit(UserAccount userAccount);

    public void visit(UserGroup userGroup);
}
