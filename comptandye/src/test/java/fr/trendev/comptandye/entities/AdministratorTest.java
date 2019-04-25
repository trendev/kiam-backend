/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.useraccount.entities.UserAccountType;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class AdministratorTest {

    public AdministratorTest() {
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
        assert UserAccountType.ADMINISTRATOR.equals(instance.getCltype());

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
        assert UserAccountType.ADMINISTRATOR.equals(instance.getCltype());
    }

}
