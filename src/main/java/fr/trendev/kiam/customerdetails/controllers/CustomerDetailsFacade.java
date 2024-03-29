package fr.trendev.kiam.customerdetails.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.customerdetails.entities.CustomerDetails;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("customerDetails")
public class CustomerDetailsFacade extends AbstractFacade<CustomerDetails, String> {

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
    public String prettyPrintPK(String pk) {
        return pk;
    }

}
