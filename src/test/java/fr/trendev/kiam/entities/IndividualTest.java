/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.useraccount.entities.UserAccountType;
import java.util.Date;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class IndividualTest {

    public IndividualTest() {
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
        assert UserAccountType.INDIVIDUAL.equals(instance.getCltype());

    }

}
