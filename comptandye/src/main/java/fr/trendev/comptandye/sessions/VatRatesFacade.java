package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.entities.VatRates;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("vatrates")
public class VatRatesFacade extends AbstractFacade<VatRates, String> {

    @Inject
    private EntityManager em;

    public VatRatesFacade() {
        super(VatRates.class);
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
