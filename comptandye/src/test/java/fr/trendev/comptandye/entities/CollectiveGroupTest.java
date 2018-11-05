/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.address.entities.Address;
import fr.trendev.comptandye.collectivegroup.entities.CollectiveGroup;
import fr.trendev.comptandye.professional.entities.Professional;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class CollectiveGroupTest {

    public CollectiveGroupTest() {
    }

    /**
     * Test of getId method, of class CollectiveGroup.
     */
    @Test
    public void testConstructors() {
        CollectiveGroup instance = new CollectiveGroup();

        assert instance.getId() == null;
        assert instance.getGroupName() == null;
        assert instance.getPhone() == null;
        assert instance.getAddress() != null;
        assert instance.getProfessional() == null;
        assert instance.getCollectiveGroupBills() != null;
        assert instance.getCollectiveGroupBills().isEmpty() == true;

        String groupName = "Collective Group";

        instance = new CollectiveGroup(groupName, "0123456789",
                new Professional());

        assert instance.getId() == null;
        assert instance.getGroupName().equals(groupName);
        assert instance.getPhone().equals("0123456789");
        assert instance.getAddress() != null;
        assert instance.getProfessional() != null;
        assert instance.getCollectiveGroupBills() != null;
        assert instance.getCollectiveGroupBills().isEmpty() == true;

        Address address = new Address();

        instance = new CollectiveGroup(groupName, "0123456789", address,
                new Professional());

        assert instance.getId() == null;
        assert instance.getGroupName().equals(groupName);
        assert instance.getPhone().equals("0123456789");
        assert instance.getAddress().equals(address);
        assert instance.getProfessional() != null;
        assert instance.getCollectiveGroupBills() != null;
        assert instance.getCollectiveGroupBills().isEmpty() == true;
    }
}
