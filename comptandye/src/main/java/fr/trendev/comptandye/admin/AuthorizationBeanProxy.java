/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.admin;

import fr.trendev.comptandye.administrator.entities.Administrator;
import fr.trendev.comptandye.individual.entities.Individual;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.useraccount.entities.UserAccount;
import fr.trendev.comptandye.usergroup.entities.UserGroup;
import fr.trendev.comptandye.administrator.controllers.AdministratorFacade;
import fr.trendev.comptandye.individual.controllers.IndividualFacade;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.useraccount.controllers.UserAccountFacade;
import fr.trendev.comptandye.usergroup.controllers.UserGroupFacade;
import fr.trendev.comptandye.usergroup.controllers.ProvideUserGroupVisitor;
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
