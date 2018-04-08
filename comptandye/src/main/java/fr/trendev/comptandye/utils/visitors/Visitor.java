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
import fr.trendev.comptandye.entities.Sale;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.entities.UserGroup;

/**
 *
 * @author jsie
 */
public interface Visitor<T> {

    public T visit(Address address);

    public T visit(Administrator administrator);

    public T visit(Bill bill);

    public T visit(Business business);

    public T visit(Category category);

    public T visit(Client client);

    public T visit(ClientBill clientBill);

    public T visit(CollectiveGroup collectiveGroup);

    public T visit(CollectiveGroupBill collectiveGroupBill);

    public T visit(Customer customer);

    public T visit(CustomerDetails customerDetails);

    public T visit(Expense expense);

    public T visit(Individual individual);

    public T visit(IndividualBill individualBill);

    public T visit(Offering offering);

    public T visit(Pack pack);

    public T visit(Payment payment);

    public T visit(PaymentMode paymentMode);

    public T visit(PurchasedOffering purchasedOffering);

    public T visit(Professional professional);

    public T visit(Sale sale);

    public T visit(Service service);

    public T visit(SocialNetworkAccounts socialNetworkAccounts);

    public T visit(UserAccount userAccount);

    public T visit(UserGroup userGroup);
}
