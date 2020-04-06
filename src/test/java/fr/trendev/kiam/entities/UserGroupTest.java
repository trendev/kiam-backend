/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class UserGroupTest {

    public UserGroupTest() {
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

        String input = "{\"name\":\"Stars\",\"description\":\"This is the TOP 10 contributors group\",\"userAccounts\":[{\"email\":\"lew.ashby@californication.com\",\"password\":\"EUrVrX4nfmYYFxpMyRX93OlkJxNZv9mkMGfirZKbhWI=\",\"username\":\"Lew\",\"uuid\":\"PRO_8d1b632f2d134a57abb93cd73360ce7e\",\"registrationDate\":1503762652756},{\"email\":\"hank.moody@hella.com\",\"password\":\"ts15qkBmihdtvmkKXPgVmbPGeyQU6aKd5XNd5HwOzu0=\",\"username\":\"Hank\",\"uuid\":\"ADMIN_5575788fbff249d1b437c87e35dc64c5\",\"registrationDate\":1503762652756},{\"email\":\"john.doe@domain.com\",\"password\":\"mZWR4R0bp5EPs9xfOwUPu3n/06LOL+wHK6BuUBsHgQM=\",\"username\":\"X\",\"uuid\":\"ADMIN_6c8c91036cab4d77b0852697b54e5cf6\",\"registrationDate\":1503762652756},{\"email\":\"julien.sie@gmail.com\",\"password\":\"RrYJsV8xV7fsJkzgrFqGwiZzvigeFan6e0ANYPcJhrI=\",\"username\":\"jsie\",\"uuid\":\"ADMIN_04fab928429049758fa05efc88887dde\",\"registrationDate\":1503762652756}]}";

        System.out.println("Deserializing " + input);
        UserGroup grp = new ObjectMapper().readerFor(UserGroup.class).readValue(
                input);

        assert "Stars".equals(grp.getName());
        assert "This is the TOP 10 contributors group".equals(grp.
                getDescription());
        /**
         * userAccounts field is initialized but ignored during deserialization
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
