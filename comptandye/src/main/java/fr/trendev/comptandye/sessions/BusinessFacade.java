package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Business;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("business")
public class BusinessFacade extends AbstractFacade<Business, String> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BusinessFacade() {
        super(Business.class);
    }

}
