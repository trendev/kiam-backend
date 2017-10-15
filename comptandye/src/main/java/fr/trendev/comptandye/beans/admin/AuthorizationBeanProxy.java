/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans.admin;

import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.AdministratorFacade;
import fr.trendev.comptandye.sessions.IndividualFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.UserAccountFacade;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.visitors.ProvideUserGroupVisitor;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 *
 * @author jsie
 */
@Stateless
public class AuthorizationBeanProxy {

    @Inject
    private AdministratorFacade administratorFacade;

    @Inject
    private IndividualFacade individualFacade;

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    UserGroupFacade userGroupFacade;

    @Inject
    private UserAccountFacade userAccountFacade;

    @Inject
    private transient ProvideUserGroupVisitor visitor;

    public boolean processAuthorizationChange(UserAccount user) {
        UserAccount admin = userAccountFacade.edit(user);
        UserGroup grp = user.accept(visitor);

        if (user.isBlocked()) {
            return admin.getUserGroups().remove(grp) & grp.
                    getUserAccounts().remove(admin);
        } else {
            return admin.getUserGroups().add(grp) & grp.
                    getUserAccounts().add(admin);
        }
    }

    public List<Administrator> getAdministrators() {
        return administratorFacade.findAll();
    }

    public List<Individual> getIndividuals() {
        return individualFacade.findAll();
    }

    public List<Professional> getProfessionals() {
        return professionalFacade.findAll();
    }

}
