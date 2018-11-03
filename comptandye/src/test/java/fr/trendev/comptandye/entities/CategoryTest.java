/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.entities;

import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.category.entities.Category;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class CategoryTest {

    public CategoryTest() {
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
     * Test of getId method, of class Category.
     */
    @Test
    public void testConstructors() {
        Category instance = new Category();

        assert instance.getId() == null;
        assert instance.getDescription() == null;
        assert instance.getName() == null;
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
        assert instance.getProfessional() == null;

        String description = "This a test of Category";
        String name = "Category #1";

        instance = new Category(description, name, new Professional());

        assert instance.getId() == null;
        assert instance.getDescription().equals(description);
        assert instance.getName().equals(name);
        assert instance.getClients() != null;
        assert instance.getClients().isEmpty();
        assert instance.getProfessional() != null;

    }

}
