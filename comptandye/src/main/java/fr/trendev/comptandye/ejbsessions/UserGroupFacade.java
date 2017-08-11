package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.UserGroup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("userGroup")
public class UserGroupFacade extends AbstractFacade<UserGroup, String> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserGroupFacade() {
        super(UserGroup.class);
    }

}
