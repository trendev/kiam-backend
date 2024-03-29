/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.address.entities.Address;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class AddressTest {

    public AddressTest() {
    }

    @Test
    public void testConstructors() {
        Address instance = new Address();

        assert instance.getId() == null;
        assert instance.getStreet() == null;
        assert instance.getOptional() == null;
        assert instance.getPostalCode() == null;
        assert instance.getCity() == null;
        assert instance.getCountry().equals("France");

        String street = "79 Avenue de la Jonchère";
        String optional = "Appartement A113";
        String postalCode = "77600";
        String city = "Chanteloup-en-Brie";
        String country = "FRANCE";

        instance = new Address(street, optional, postalCode, city, country);

        assert instance.getId() == null;
        assert street.equals(instance.getStreet());
        assert optional.equals(instance.getOptional());
        assert postalCode.equals(instance.getPostalCode());
        assert city.equals(instance.getCity());
        assert country.equals(instance.getCountry());

        instance = new Address(street, optional, postalCode, city);
        assert instance.getId() == null;
        assert street.equals(instance.getStreet());
        assert optional.equals(instance.getOptional());
        assert postalCode.equals(instance.getPostalCode());
        assert city.equals(instance.getCity());
        assert "France".equals(instance.getCountry());

    }

}
