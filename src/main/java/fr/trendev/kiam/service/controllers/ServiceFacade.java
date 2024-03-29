package fr.trendev.kiam.service.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.offering.entities.OfferingPK;
import fr.trendev.kiam.service.entities.Service;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("service")
public class ServiceFacade extends AbstractFacade<Service, OfferingPK> {

    @Inject
    private EntityManager em;

    public ServiceFacade() {
        super(Service.class);
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
