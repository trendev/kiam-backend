/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class CollectiveGroupTest {
    
    public CollectiveGroupTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class CollectiveGroup.
     */
    @Test
    public void testConstructors() {
        CollectiveGroup instance = new CollectiveGroup();
        
        assert instance.getId() == null;
        assert instance.getGroupName() == null;
        assert instance.getAddress() == null;
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
        
        String groupName = "Collective Group";
        
        instance = new CollectiveGroup(groupName);
        
        assert instance.getId() == null;
        assert instance.getGroupName().equals(groupName);
        assert instance.getAddress() == null;
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
        
        Address address = new Address();
        
        instance = new CollectiveGroup(groupName, address);
        
        assert instance.getId() == null;
        assert instance.getGroupName().equals(groupName);
        assert instance.getAddress().equals(address);
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
    }
    
}
