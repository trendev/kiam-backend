package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.CollectiveGroup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("collectiveGroup")
public class CollectiveGroupFacade extends AbstractFacade<CollectiveGroup, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CollectiveGroupFacade() {
        super(CollectiveGroup.class);
    }

}
