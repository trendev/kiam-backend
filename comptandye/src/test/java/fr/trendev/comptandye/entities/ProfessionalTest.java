/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.address.entities.Address;
import fr.trendev.comptandye.utils.UserAccountType;
import java.util.Calendar;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ProfessionalTest {

    public ProfessionalTest() {
    }

    @Test
    public void testConstructors() {
        Professional instance = new Professional();
        assert instance.getEmail() == null;
        assert instance.getPassword() == null;
        assert instance.getUsername() == null;
        assert instance.getUuid() == null;
        Date now = new Date();
        assert instance.getRegistrationDate() != null;
        assert now.equals(instance.getRegistrationDate()) || now.after(instance.
                getRegistrationDate());
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();
        assert instance.getCustomerDetails() != null;
        assert instance.getAddress() != null;
        assert instance.getSocialNetworkAccounts() != null;
        assert instance.getWebsite() == null;
        assert instance.getCompanyID() == null;
        assert instance.getVatcode() == null;
        assert instance.getCreationDate() == null;
        assert instance.getBills() != null;
        assert instance.getBills().isEmpty();
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty();
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();
        assert instance.getCollectiveGroups() != null;
        assert instance.getCollectiveGroups().isEmpty();
        assert instance.getExpenses() != null;
        assert instance.getExpenses().isEmpty();
        assert instance.getIndividuals() != null;
        assert instance.getIndividuals().isEmpty();
        assert instance.getPaymentModes() != null;
        assert instance.getPaymentModes().isEmpty();
        assert instance.isBlocked() == true;
        assert instance.getVatRates() == null;
        assert instance.getStock() != null;
        assert instance.getStock().isEmpty() == true;
        assert UserAccountType.PROFESSIONAL.equals(instance.getCltype());
        assert instance.getNotifications() != null;
        assert instance.getNotifications().isEmpty();
        assert instance.getStripeCustomerId() == null;
        assert instance.getStripeSubscriptionId() == null;
        assert instance.isTos() == false;
        assert instance.getRegistrationDate() != null;

        String email = "professional@domain.com";
        String password = "encrypted_pwd";
        String username = "PRO01";
        String uuid = "PRO_0001";
        instance = new Professional(email, password, username, uuid);
        instance.setBlocked(false);

        assert instance.getEmail().equals(email);
        assert instance.getPassword().equals(password);
        assert instance.getUsername().equals(username);
        assert instance.getUuid().equals(uuid);
        assert instance.getRegistrationDate() != null;
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();
        assert instance.getCustomerDetails() != null;
        assert instance.getAddress() != null;
        assert instance.getSocialNetworkAccounts() != null;
        assert instance.getWebsite() == null;
        assert instance.getCompanyName() == null;
        assert instance.getCompanyID() == null;
        assert instance.getVatcode() == null;
        assert instance.getCreationDate() == null;
        assert instance.getBills() != null;
        assert instance.getBills().isEmpty();
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty();
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();
        assert instance.getCollectiveGroups() != null;
        assert instance.getCollectiveGroups().isEmpty();
        assert instance.getExpenses() != null;
        assert instance.getExpenses().isEmpty();
        assert instance.getIndividuals() != null;
        assert instance.getIndividuals().isEmpty();
        assert instance.getPaymentModes() != null;
        assert instance.getPaymentModes().isEmpty();
        assert instance.isBlocked() == false;
        assert instance.getVatRates() == null;
        assert instance.getStock() != null;
        assert instance.getStock().isEmpty() == true;
        assert UserAccountType.PROFESSIONAL.equals(instance.getCltype());
        assert instance.getNotifications() != null;
        assert instance.getNotifications().isEmpty();

        String website = "http://www.mycompany.com";
        String companyName = "MyCompany";
        String companyID = "XXXX123456FR";
        String vatcode = "FRTVA1234564";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        Date creationDate = cal.getTime();

        instance = new Professional(email, password, username, uuid,
                new CustomerDetails(), new Address(),
                new SocialNetworkAccounts(), website, companyName, companyID,
                vatcode, creationDate);
        instance.setBlocked(false);

        assert instance.getEmail().equals(email);
        assert instance.getPassword().equals(password);
        assert instance.getUsername().equals(username);
        assert instance.getUuid().equals(uuid);
        assert instance.getRegistrationDate() != null;
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();
        assert instance.getCustomerDetails() != null;
        assert instance.getAddress() != null;
        assert instance.getSocialNetworkAccounts() != null;
        assert instance.getWebsite().equals(website);
        assert instance.getCompanyName().equals(companyName);
        assert instance.getCompanyID().equals(companyID);
        assert instance.getVatcode().equals(vatcode);
        assert instance.getCreationDate() != null;
        assert instance.getCreationDate().before(now);
        assert instance.getBills() != null;
        assert instance.getBills().isEmpty();
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
        assert instance.getBusinesses() != null;
        assert instance.getBusinesses().isEmpty();
        assert instance.getOfferings() != null;
        assert instance.getOfferings().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();
        assert instance.getCollectiveGroups() != null;
        assert instance.getCollectiveGroups().isEmpty();
        assert instance.getExpenses() != null;
        assert instance.getExpenses().isEmpty();
        assert instance.getIndividuals() != null;
        assert instance.getIndividuals().isEmpty();
        assert instance.getPaymentModes() != null;
        assert instance.getPaymentModes().isEmpty();
        assert instance.isBlocked() == false;
        assert instance.getVatRates() == null;
        assert instance.getStock() != null;
        assert instance.getStock().isEmpty() == true;
        assert UserAccountType.PROFESSIONAL.equals(instance.getCltype());
        assert instance.getNotifications() != null;
        assert instance.getNotifications().isEmpty();

    }

}
