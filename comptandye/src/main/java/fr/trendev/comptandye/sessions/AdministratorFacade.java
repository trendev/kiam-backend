package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Administrator;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("administrator")
public class AdministratorFacade extends AbstractFacade<Administrator, String> {

    @Inject
    private EntityManager em;

    public AdministratorFacade() {
        super(Administrator.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(String pk) {
        return pk;
    }

}
