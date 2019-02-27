/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt;

import fr.trendev.comptandye.security.controllers.jwt.dto.mock.MockErrorsJWTWhiteMapDTO;
import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.MockJWTWhiteMapDTO;
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
            .from(JWTWhiteMap.class, MockErrorsJWTWhiteMapDTO.class)
            .inject(this).build();

    @Inject
    JWTWhiteMap jwtwm;

    public JWTWhiteMapTestWithExceptions() {
    }

    @Test
    public void testInit() {
        Assertions.assertDoesNotThrow(() -> jwtwm.init());
        Assertions.assertTrue(jwtwm.getMap().isEmpty(),
                "jwtwm.getMap().isEmpty() should be true");
        // wait the response from the DTO, assuming a latency
        LOG.info("Waiting " + MockJWTWhiteMapDTO.LATENCY * 2 + " ms "
                + "/ Thread = " + Thread.currentThread().getName());
        Assertions.assertDoesNotThrow(() -> Thread.sleep(
                MockJWTWhiteMapDTO.LATENCY * 2));

        Assertions.assertDoesNotThrow(() -> jwtwm.getMap());
        Assertions.assertNotNull(jwtwm.getMap());
        Assertions.assertTrue(jwtwm.getMap().isEmpty(),
                "jwtwm.getMap().isEmpty() should be true because of the thrown Exception");
        Assertions.assertTrue(jwtwm.getMap().size() == 0,
                "jwtwm.getMap().size() should be 0 because of the thrown Exception");
    }

}
