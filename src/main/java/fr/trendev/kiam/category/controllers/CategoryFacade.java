package fr.trendev.kiam.category.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.category.entities.CategoryPK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("category")
public class CategoryFacade extends AbstractFacade<Category, CategoryPK> {

    @Inject
    private EntityManager em;

    public CategoryFacade() {
        super(Category.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(CategoryPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getId());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

}
