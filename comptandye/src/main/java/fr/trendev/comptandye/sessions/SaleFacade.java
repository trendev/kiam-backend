package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Sale;
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
