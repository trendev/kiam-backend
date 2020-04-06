package fr.trendev.kiam.usergroup.controllers;

import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.usergroup.entities.UserGroup;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("userGroup")
public class UserGroupFacade extends AbstractFacade<UserGroup, String> {

    @Inject
    private EntityManager em;

    public UserGroupFacade() {
        super(UserGroup.class);
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
