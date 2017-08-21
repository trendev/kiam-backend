/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ProfessionalTest {

    public ProfessionalTest() {
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
    public void testConstructors() {
        Professional instance = new Professional();
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();
        assert instance.getRegistrationDate() != null;
        Date now = new Date();
        assert now.equals(instance.getRegistrationDate()) || now.after(instance.
                getRegistrationDate());
    }

}
