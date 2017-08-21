/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ClientTest {

    public ClientTest() {
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
        Client instance = new Client();

        assert instance.getId() == null;
        assert instance.getEmail() == null;
        assert instance.getSocialNetworkAccounts() == null;
        assert instance.getCustomerDetails() == null;
        assert instance.getAddress() == null;
        assert instance.getProfessional() == null;
        assert instance.getClientBills() != null;
        assert instance.getClientBills().isEmpty();
        assert instance.getCollectiveGroups() != null;
        assert instance.getCollectiveGroups().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();

        String email = "client@gmail.com";

        instance = new Client(email);

        assert instance.getId() == null;
        assert instance.getEmail().equals(email);
        assert instance.getSocialNetworkAccounts() == null;
        assert instance.getCustomerDetails() == null;
        assert instance.getAddress() == null;
        assert instance.getProfessional() == null;
        assert instance.getClientBills() != null;
        assert instance.getClientBills().isEmpty();
        assert instance.getCollectiveGroups() != null;
        assert instance.getCollectiveGroups().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();

        instance = new Client(email, new SocialNetworkAccounts(),
                new CustomerDetails(), new Address(), new Professional());

        int size = 100;
        instance.setCategories(IntStream
                .range(0, size)
                .mapToObj(i -> new Category("This Category#" + i, "Category #"
                        + i))
                .collect(Collectors.toList())
        );

        assert instance.getId() == null;
        assert instance.getEmail().equals(email);
        assert instance.getSocialNetworkAccounts() != null;
        assert instance.getCustomerDetails() != null;
        assert instance.getAddress() != null;
        assert instance.getProfessional() != null;
        assert instance.getClientBills() != null;
        assert instance.getClientBills().isEmpty();
        assert instance.getCollectiveGroups() != null;
        assert instance.getCollectiveGroups().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().size() == size;

    }

}
