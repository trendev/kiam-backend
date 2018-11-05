/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.address.entities.Address;
import fr.trendev.comptandye.customerdetails.entities.CustomerDetails;
import fr.trendev.comptandye.socialnetworkaccounts.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.usergroup.entities.Customer;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class CustomerTest {

    public CustomerTest() {
    }

    /**
     * Test of getCustomerDetails method, of class Customer.
     */
    @Test
    public void testConstructors() {

        Customer instance = new CustomerImpl();

        assert instance.getEmail() == null;
        assert instance.getPassword() == null;
        assert instance.getUsername() == null;
        assert instance.getUuid() == null;
        assert instance.getRegistrationDate() != null;
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();
        assert instance.getCustomerDetails() != null;
        assert instance.getAddress() != null;
        assert instance.getSocialNetworkAccounts() != null;

        String email = "customer@domain.com";
        String password = "encrypted_pwd";
        String username = "Customer01";
        String uuid = "PRO_0001";
        instance = new CustomerImpl(email, password, username, uuid);

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

        instance = new CustomerImpl(email, password, username, uuid,
                new CustomerDetails(), new Address(),
                new SocialNetworkAccounts());

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

    }

    public class CustomerImpl extends Customer {

        public CustomerImpl(String email, String password, String username,
                String uuid) {
            super(email, password, username, uuid);
        }

        public CustomerImpl(String email, String password, String username,
                String uuid, CustomerDetails customerDetails, Address address,
                SocialNetworkAccounts socialNetworkAccounts) {
            super(email, password, username, uuid, customerDetails, address,
                    socialNetworkAccounts);
        }

        public CustomerImpl() {
        }

    }

}
