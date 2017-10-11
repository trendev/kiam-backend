package fr.trendev.comptandye.sessions;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("userAccount")
public class UserAccountFacade {

    @Inject
    private EntityManager em;

    public UserAccountFacade() {
        super();
    }

    public String getUserAccountType(String email) {
        return null;
    }

}
