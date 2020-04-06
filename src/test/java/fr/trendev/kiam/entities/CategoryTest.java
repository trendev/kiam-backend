/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.entities;

import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.professional.entities.Professional;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class CategoryTest {

    public CategoryTest() {
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
