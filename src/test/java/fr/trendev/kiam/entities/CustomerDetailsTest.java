/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.customerdetails.entities.CustomerDetails;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class CustomerDetailsTest {

    public CustomerDetailsTest() {
    }

    @Test
    public void testConstructors() {
        CustomerDetails instance = new CustomerDetails();

        assert instance.getId() == null;
        assert instance.getFirstName() == null;
        assert instance.getLastName() == null;
        assert instance.getNickname() == null;
        assert instance.getPhone() == null;
        assert instance.getBirthdate() == null;
        assert instance.getSex() == 'F';
        assert instance.getPicturePath() == null;
        assert instance.getComments() != null;
        assert instance.getComments().isEmpty();
        assert instance.getJobrole() == null;

        String firstName = "John";
        String lastName = "Doe";
        String nickname = "X";
        String phone = "+33123456789";
        Calendar cal = Calendar.getInstance();
        cal.set(1982, 10, 28, 06, 28);
        Date birthdate = cal.getTime();
        char sex = 'H';
        String picturePath = "/home/user01/Pictures/profil.jpg";

        List<String> comments = IntStream.range(0, 100).mapToObj(i
                -> ("Comment #" + (i + 1))).
                collect(Collectors.toList());

        instance = new CustomerDetails(firstName, lastName, nickname, phone,
                birthdate, sex, picturePath);
        instance.setComments(comments);
        instance.setJobrole("jobrole");

        assert instance.getId() == null;
        assert firstName.equals(instance.getFirstName());
        assert lastName.equals(instance.getLastName());
        assert nickname.equals(instance.getNickname());
        assert phone.equals(instance.getPhone());
        assert birthdate.equals(instance.getBirthdate());
        assert sex == instance.getSex();
        assert picturePath.equals(instance.getPicturePath());
        assert instance.getComments() != null;
        assert instance.getComments().isEmpty() == false;
        assert instance.getComments().containsAll(comments);
        assert "jobrole".equals(instance.getJobrole());
    }

}
