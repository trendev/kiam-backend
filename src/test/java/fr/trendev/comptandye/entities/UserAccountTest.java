/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.useraccount.entities.UserAccount;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class UserAccountTest {

    public UserAccountTest() {
    }

    /**
     * Test of getEmail method, of class UserAccount.
     */
    @Test
    public void testConstructors() {

        UserAccount instance = new UserAccountImpl();
        assert instance.getEmail() == null;
        assert instance.getPassword() == null;
        assert instance.getUsername() == null;
        assert instance.getUuid() == null;
        assert instance.getRegistrationDate() != null;
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();

        String email = "user@domain.com";
        String password = "encrypted_pwd";
        String username = "User01";
        String uuid = "USER_0001";
        instance = new UserAccountImpl(email, password, username, uuid);

        assert instance.getEmail().equals(email);
        assert instance.getPassword().equals(password);
        assert instance.getUsername().equals(username);
        assert instance.getUuid().equals(uuid);
        assert instance.getRegistrationDate() != null;
        assert instance.getUserGroups() != null;
        assert instance.getUserGroups().isEmpty();

    }

    public class UserAccountImpl extends UserAccount {

        public UserAccountImpl() {
            super();
        }

        public UserAccountImpl(String email, String password, String username,
                String uuid) {
            super(email, password, username, uuid);

        }
    }

}
