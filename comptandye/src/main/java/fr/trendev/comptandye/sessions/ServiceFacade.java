package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Service;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("service")
public class ServiceFacade extends AbstractFacade<Service, OfferingPK> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ServiceFacade() {
        super(Service.class);
    }

}
