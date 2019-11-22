package fr.trendev.comptandye.address.controllers;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.address.entities.Address;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("address")
public class AddressFacade extends AbstractFacade<Address, String> {

    @Inject
    private EntityManager em;

    public AddressFacade() {
        super(Address.class);
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
