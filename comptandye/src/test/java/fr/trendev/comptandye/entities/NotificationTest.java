/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.notification.entities.Notification;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class NotificationTest {

    public NotificationTest() {
    }

    @Test
    public void testConstructor() {
        Notification instance = new NotificationImpl();

        assert instance.getId() == null;
        assert instance.getCltype() == null;
        assert instance.isChecked() == false;
        assert instance.getLevelRank() == null;
    }

    public class NotificationImpl extends Notification {
    }

}
