/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import fr.trendev.kiam.security.controllers.SlackAuthenticationEventController;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class SlackAuthenticationEventControllerTest {

    private final SlackAuthenticationEventController saec = new SlackAuthenticationEventController();

    public SlackAuthenticationEventControllerTest() {
    }

    @Test
    public void testLogin() {
    }

    @Test
    public void testLogout() {
    }

    @Test
    public void serializable() {
        Assertions.assertDoesNotThrow(() -> {
            try {

                if (!Serializable.class.isInstance(saec)) {
                    throw new IllegalStateException();
                }

                ByteArrayOutputStream bf = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bf);
                oos.writeObject(saec);
                oos.close();
            } catch (Exception ex) {
                throw new Exception(ex);
            }
        });

    }

}
