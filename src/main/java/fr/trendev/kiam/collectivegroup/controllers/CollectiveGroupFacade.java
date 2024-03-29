package fr.trendev.kiam.collectivegroup.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroup;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroupPK;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("collectiveGroup")
public class CollectiveGroupFacade extends AbstractFacade<CollectiveGroup, CollectiveGroupPK> {

    @Inject
    private EntityManager em;

    public CollectiveGroupFacade() {
        super(CollectiveGroup.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(CollectiveGroupPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getId());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

}
