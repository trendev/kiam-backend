package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.CategoryPK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("category")
public class CategoryFacade extends AbstractFacade<Category, CategoryPK> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CategoryFacade() {
        super(Category.class);
    }

}
