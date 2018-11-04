/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.thresholdalert.entities.ThresholdAlert;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.notification.entities.NotificationLevelEnum;
import fr.trendev.comptandye.notification.entities.NotificationType;
import fr.trendev.comptandye.utils.ThresholdAlertQualifierEnum;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ThresholdAlertTest {

    public ThresholdAlertTest() {
    }

    @Test
    public void testConstructor() {
        ThresholdAlert instance = new ThresholdAlert();
        assert instance.getId() == null;
        assert NotificationType.THRESHOLD_ALERT.equals(instance.getCltype());
        assert instance.isChecked() == false;
        assert instance.getLevelRank() == null;
        assert instance.getBarcode() == null;
        assert instance.getDescription() == null;
        assert instance.getThreshold() == 0;
        assert instance.getQty() == 0;
        assert instance.getQualifier() == null;

        Professional pro = new Professional();
        String barcode = "barcode";
        String description = "this is a description";
        String brand = "TOP BRAND";
        int threshold = 5;
        int qty = 3;

        instance = new ThresholdAlert(
                NotificationLevelEnum.WARNING,
                pro,
                barcode,
                description,
                brand,
                threshold, qty,
                ThresholdAlertQualifierEnum.WARNING);

        assert instance.getId() == null;
        assert pro.getNotifications().contains(instance);
        assert NotificationType.THRESHOLD_ALERT.equals(instance.getCltype());
        assert instance.isChecked() == false;
        assert NotificationLevelEnum.WARNING.equals(instance.getLevelRank());
        assert barcode.equals(instance.getBarcode());
        assert description.equals(instance.getDescription());
        assert brand.equals(instance.getBrand());
        assert instance.getThreshold() == threshold;
        assert instance.getQty() == qty;
        assert ThresholdAlertQualifierEnum.WARNING.equals(instance.
                getQualifier());

    }

}
