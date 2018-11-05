/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.offering.entities.OfferingType;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.purchasedoffering.entities.OfferingSnapshot;
import fr.trendev.comptandye.service.entities.Service;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jsie
 */
public class OfferingSnapshotTest {

    public OfferingSnapshotTest() {
    }

    @Test
    public void testConstructors() {
        OfferingSnapshot os = new OfferingSnapshot();

        assert os.getCltype() == null;
        assert os.getName() == null;
        assert os.getPrice() == 0;
        assert os.getDuration() == 0;
        assert os.getBusinesses() != null;
        assert os.getBusinesses().isEmpty() == true;

        Service service = new Service("test_service", 1000, 120,
                new Professional());
        service.setCltype(OfferingType.SERVICE);
        service.setShortname("ts");
        service.getBusinesses().add(new Business("business#1"));
        service.getBusinesses().add(new Business("business#2"));

        os = new OfferingSnapshot(service);

        assert os.getCltype().equals(service.getCltype());
        assert os.getName().equals(service.getName());
        assert os.getShortname().equals(service.getShortname());
        assert os.getPrice() == service.getPrice();
        assert os.getDuration() == service.getDuration();
        assert os.getBusinesses() != null;
        assert os.getBusinesses().isEmpty() == false;
        assert os.getBusinesses().size() == service.getBusinesses().size();

    }

    @Test
    public void testBusinessesLink() {
        Service service = new Service("test_service", 1000, 120,
                new Professional());
        service.setCltype(OfferingType.SERVICE);
        service.getBusinesses().add(new Business("business#1"));
        service.getBusinesses().add(new Business("business#2"));

        OfferingSnapshot os = new OfferingSnapshot(service);

        assert os.getBusinesses() != null;
        assert os.getBusinesses().isEmpty() == false;
        assert os.getBusinesses().size() == service.getBusinesses().size();
        assert os.getBusinesses().containsAll(service.getBusinesses());

        int size = service.getBusinesses().size();

        service.getBusinesses().remove(0);

        assert service.getBusinesses().size() == size - 1;
        assert os.getBusinesses().size() == size - 1;
    }

}
