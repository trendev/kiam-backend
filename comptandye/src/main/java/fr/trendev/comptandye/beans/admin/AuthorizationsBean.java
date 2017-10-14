/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans.admin;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.sessions.AdministratorFacade;
import fr.trendev.comptandye.sessions.IndividualFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.UserAccountFacade;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author jsie
 */
@Named
@SessionScoped
public class AuthorizationsBean extends CommonUsersBean {

    @Inject
    ActiveSessionTracker tracker;

    @Inject
    UserGroupFacade userGroupFacade;

    @Inject
    private AdministratorFacade administratorFacade;

    @Inject
    private IndividualFacade individualFacade;

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    private UserAccountFacade userAccountFacade;

    private List<Administrator> administrators;

    private List<Individual> individuals;

    private List<Professional> professionals;

    @PostConstruct
    public void init() {
        this.administrators = administratorFacade.findAll();
        this.individuals = individualFacade.findAll();
        this.professionals = professionalFacade.findAll();
    }

    public AdministratorFacade getAdministratorFacade() {
        return administratorFacade;
    }

    public IndividualFacade getIndividualFacade() {
        return individualFacade;
    }

    public ProfessionalFacade getProfessionalFacade() {
        return professionalFacade;
    }

    public List<Administrator> getAdministrators() {
        return administrators;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public List<Professional> getProfessionals() {
        return professionals;
    }

    public boolean hideDisconnectButton(UserAccount user) {
        return false;
//return Optional.ofNullable(tracker.)
    }
}
