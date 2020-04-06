package fr.trendev.kiam.sale.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.offering.entities.OfferingPK;
import fr.trendev.kiam.sale.entities.Sale;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("sale")
public class SaleFacade extends AbstractFacade<Sale, OfferingPK> {

    @Inject
    private EntityManager em;

    public SaleFacade() {
        super(Sale.class);
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
