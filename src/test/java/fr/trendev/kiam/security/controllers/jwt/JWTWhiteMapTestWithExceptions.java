/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt;

import fr.trendev.kiam.security.controllers.jwt.JWTWhiteMap;
import fr.trendev.kiam.security.controllers.MockAuthenticationEventController;
import fr.trendev.kiam.security.controllers.jwt.dto.mock.MockErrorsJWTWhiteMapDTO;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class JWTWhiteMapTestWithExceptions {

    private static final Logger LOG = Logger.getLogger(
            JWTWhiteMapTestWithExceptions.class.
                    getName());

    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(JWTWhiteMap.class,
                    MockAuthenticationEventController.class,
                    MockErrorsJWTWhiteMapDTO.class)
            .inject(this).build();

    @Inject
    JWTWhiteMap jwtwm;

    public JWTWhiteMapTestWithExceptions() {
    }

    @Test
    public void testInit() {
        Assertions.assertDoesNotThrow(() -> jwtwm.init());
        Assertions.assertTrue(jwtwm.getMap().isEmpty(),
                "jwtwm.getMap() should be empty");

        Assertions.assertDoesNotThrow(() -> jwtwm.getMap());
        Assertions.assertNotNull(jwtwm.getMap());
        Assertions.assertTrue(jwtwm.getMap().isEmpty(),
                "jwtwm.getMap().isEmpty() should be true because of the thrown Exception");
        Assertions.assertTrue(jwtwm.getMap().size() == 0,
                "jwtwm.getMap().size() should be 0 because of the thrown Exception");
    }

}
