package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.entities.CustomerDetails;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("customerDetails")
public class CustomerDetailsFacade extends AbstractFacade<CustomerDetails, Long> {

    @Inject
    private EntityManager em;

    public CustomerDetailsFacade() {
        super(CustomerDetails.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(Long pk) {
        return pk.toString();
    }

}
