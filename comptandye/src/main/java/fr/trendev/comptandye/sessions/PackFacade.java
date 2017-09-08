package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Pack;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("pack")
public class PackFacade extends AbstractFacade<Pack, OfferingPK> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PackFacade() {
        super(Pack.class);
    }

}
