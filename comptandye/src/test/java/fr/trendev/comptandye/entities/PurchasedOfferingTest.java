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
public class PurchasedOfferingTest {

    public PurchasedOfferingTest() {
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

    @Test
    public void testConstructor() {
        PurchasedOffering po = new PurchasedOffering();

        assert po.getId() == null;
        assert po.getQty() == 1;
        assert po.getOffering() == null;

        po = new PurchasedOffering(5, new Service());
        assert po.getId() == null;
        assert po.getQty() == 5;
        assert po.getOffering() != null;
    }

}
