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
public class AdministratorTest {

    public AdministratorTest() {
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
        Administrator instance = new Administrator();
        assert instance.getEmail() == null;
        assert instance.getPassword() == null;
        assert instance.getUsername() == null;
        assert instance.getUuid() == null;
        assert instance.getRegistrationDate() != null;
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();
        assert instance.isBlocked() == true;

        String email = "admin@domain.com";
        String password = "encrypted_pwd";
        String username = "Admin01";
        String uuid = "ADMIN_0001";
        instance = new Administrator(email, password, username, uuid);
        instance.setBlocked(false);

        assert instance.getEmail().equals(email);
        assert instance.getPassword().equals(password);
        assert instance.getUsername().equals(username);
        assert instance.getUuid().equals(uuid);
        assert instance.getRegistrationDate() != null;
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();
        assert instance.isBlocked() == false;
    }

}
