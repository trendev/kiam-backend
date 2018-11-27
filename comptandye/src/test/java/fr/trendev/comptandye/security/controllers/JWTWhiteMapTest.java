/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fr.trendev.comptandye.security.entities.JWTRecord;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import org.jboss.weld.junit4.WeldInitiator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class JWTWhiteMapTest {
    
    @Rule
    public WeldInitiator weld = WeldInitiator
            .from(JWTWhiteMap.class)
            .inject(this).build();
    
    @Inject
    JWTWhiteMap jwtwm;
    
    public JWTWhiteMapTest() {
    }
    
    @Test
    public void testInjection() {
        Assertions.assertNotNull(jwtwm);
        Assertions.assertDoesNotThrow(() -> jwtwm.getMap());
    }
    
    @Test
    public void testInit() {
    }
    
    @Test
    public void testClose() {
    }
    
    @Test
    public void testGetMap() {
        Map<String, Set<JWTRecord>> map = jwtwm.getMap();
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());
    }
    
}
