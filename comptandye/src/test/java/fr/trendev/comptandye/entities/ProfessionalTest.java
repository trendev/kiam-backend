/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import java.util.Calendar;
import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ProfessionalTest {

    public ProfessionalTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
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
        assert instance.getVATcode() == null;
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

        String email = "professional@domain.com";
        String password = "encrypted_pwd";
        String username = "PRO01";
        String uuid = "PRO_0001";
        instance = new Professional(email, password, username, uuid);

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
        assert instance.getVATcode() == null;
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

        String website = "http://www.mycompany.com";
        String companyName = "MyCompany";
        String companyID = "XXXX123456FR";
        String VATcode = "FRTVA1234564";
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        Date creationDate = cal.getTime();

        instance = new Professional(email, password, username, uuid,
                new CustomerDetails(), new Address(),
                new SocialNetworkAccounts(), website, companyName, companyID,
                VATcode, creationDate);

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
        assert instance.getVATcode().equals(VATcode);
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
    }

}
