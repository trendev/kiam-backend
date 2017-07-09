package fr.trendev.comptandye.ejbsessions;

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

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerDetailsFacade() {
        super(CustomerDetails.class);
    }

}
