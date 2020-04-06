package fr.trendev.kiam.socialnetworkaccounts.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.socialnetworkaccounts.entities.SocialNetworkAccounts;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("socialNetworkAccounts")
public class SocialNetworkAccountsFacade extends AbstractFacade<SocialNetworkAccounts, String> {

    @Inject
    private EntityManager em;

    public SocialNetworkAccountsFacade() {
        super(SocialNetworkAccounts.class);
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
