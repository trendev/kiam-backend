package fr.trendev.kiam.pack.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.offering.entities.OfferingPK;
import fr.trendev.kiam.pack.entities.Pack;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("pack")
public class PackFacade extends AbstractFacade<Pack, OfferingPK> {

    @Inject
    private EntityManager em;

    public PackFacade() {
        super(Pack.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(OfferingPK pk) {
        StringBuilder sb = new StringBuilder();
        sb.append(pk.getId());
        sb.append("?professional=").append(pk.getProfessional());
        return sb.toString();
    }

}
