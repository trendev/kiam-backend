/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class UserGroupTest {

    public UserGroupTest() {
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

    @Test//(expected = JsonMappingException.class)
    public void testSerialize() throws JsonProcessingException, IOException {
        UserGroup group = new UserGroup();
        group.setName("Administrator");
        group.setDescription("ADMIN GROUP");
        group.setUserAccounts(new ArrayList<>());

        Administrator admin01 = new Administrator();
        admin01.setEmail("admin01@mycompany.com");
        admin01.setUserGroups(new ArrayList<>());

        Administrator admin02 = new Administrator();
        admin02.setEmail("admin02@mycompany.com");
        admin02.setUserGroups(new ArrayList<>());

        group.getUserAccounts().add(admin01);
        group.getUserAccounts().add(admin02);

        admin01.getUserGroups().add(group);
        admin02.getUserGroups().add(group);

        ObjectMapper om = new ObjectMapper();
        String result = om.writeValueAsString(group);
        System.out.println("result = " + result);

        assert !result.contains(admin01.getEmail());
        assert !result.contains(admin02.getEmail());

        String input = "{\"name\":\"Stars\",\"description\":\"This is the TOP 10 contributors group\",\"userAccounts\":[]}";
        UserGroup grp = new ObjectMapper().readerFor(UserGroup.class).readValue(
                input);

        assert "Stars".equals(grp.getName());
        assert "This is the TOP 10 contributors group".equals(grp.
                getDescription());
        /**
         * if there is no field userAccounts : grp.getUserAccounts() will be
         * null !
         */
        assert grp.getUserAccounts() != null;
        assert grp.getUserAccounts().isEmpty();
    }

    @Test
    public void testConstructors() {
        System.out.println("testConstructors");

        UserGroup instance = new UserGroup();

        assert instance.getName() == null;
        assert instance.getDescription() == null;
        assert instance.getUserAccounts() != null;
        assert instance.getUserAccounts().isEmpty();

        String name = "TestGrp";
        String desc = "This is a test group";
        instance = new UserGroup(name, desc);

        assert name.equals(instance.getName());
        assert desc.equals(instance.getDescription());
        assert instance.getUserAccounts() != null;
        assert instance.getUserAccounts().isEmpty();
    }
}
