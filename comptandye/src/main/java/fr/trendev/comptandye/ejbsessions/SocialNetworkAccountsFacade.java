package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("socialNetworkAccounts")
public class SocialNetworkAccountsFacade extends AbstractFacade<SocialNetworkAccounts, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public SocialNetworkAccountsFacade() {
        super(SocialNetworkAccounts.class);
    }

}
