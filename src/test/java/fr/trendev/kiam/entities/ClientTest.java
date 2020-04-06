/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.address.entities.Address;
import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.customerdetails.entities.CustomerDetails;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.socialnetworkaccounts.entities.SocialNetworkAccounts;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ClientTest {

    public ClientTest() {
    }

    @Test
    public void testConstructors() {
        Client instance = new Client();

        assert instance.getId() == null;
        assert instance.getEmail() == null;
        assert instance.getSocialNetworkAccounts() != null;
        assert instance.getCustomerDetails() != null;
        assert instance.getAddress() != null;
        assert instance.getProfessional() == null;
        assert instance.getClientBills() != null;
        assert instance.getClientBills().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();

        String email = "client@gmail.com";

        Professional pro = new Professional();

        instance = new Client(email, pro);

        assert instance.getId() == null;
        assert instance.getEmail().equals(email);
        assert instance.getSocialNetworkAccounts() != null;
        assert instance.getCustomerDetails() != null;
        assert instance.getAddress() != null;
        assert instance.getProfessional() != null;
        assert instance.getClientBills() != null;
        assert instance.getClientBills().isEmpty();
        assert instance.getCategories() != null;
        assert instance.getCategories().isEmpty();

        instance = new Client(email, new SocialNetworkAccounts(),
                new CustomerDetails(), new Address(), new Professional());

        int size = 100;
        instance.setCategories(IntStream
                .range(0, size)
                .mapToObj(i -> new Category("This Category#" + i, "Category #"
                        + i, new Professional()))
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
        assert instance.getCategories() != null;
        assert instance.getCategories().size() == size;

    }

}
