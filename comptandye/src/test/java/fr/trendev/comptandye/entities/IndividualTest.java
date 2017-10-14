/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

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
public class IndividualTest {

    public IndividualTest() {
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

    /**
     * Test of getProfessionals method, of class Individual.
     */
    @Test
    public void testConstructors() {
        Individual instance = new Individual();

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
        assert instance.getProfessionals() != null;
        assert instance.getProfessionals().isEmpty();
        assert instance.getIndividualBills() != null;
        assert instance.getIndividualBills().isEmpty();
        assert instance.isBlocked() == true;

        String email = "individual@procompany.com";
        String password = "encrypted_pwd";
        String username = "IND01";
        String uuid = "IND_0001";
        instance = new Individual(email, password, username, uuid);
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
        assert instance.getProfessionals() != null;
        assert instance.getProfessionals().isEmpty();
        assert instance.getIndividualBills() != null;
        assert instance.getIndividualBills().isEmpty();
        assert instance.isBlocked() == false;

        instance = new Individual(email, password, username, uuid,
                new CustomerDetails(), new Address(),
                new SocialNetworkAccounts());
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
        assert instance.getProfessionals() != null;
        assert instance.getProfessionals().isEmpty();
        assert instance.getIndividualBills() != null;
        assert instance.getIndividualBills().isEmpty();
        assert instance.isBlocked() == false;
    }

}
