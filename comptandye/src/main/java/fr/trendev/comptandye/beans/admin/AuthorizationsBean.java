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
import fr.trendev.comptandye.sessions.AdministratorFacade;
import fr.trendev.comptandye.sessions.IndividualFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.UserAccountFacade;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Named
@ViewScoped
public class AuthorizationsBean extends CommonUsersBean {

    private static final Logger LOG = Logger.getLogger(AuthorizationsBean.class.
            getName());

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

    public boolean renderDisconnectButton(UserAccount user) {
        return !this.getSessions(user.getEmail()).isEmpty();
    }

    public void disconnect(UserAccount user) {

        List<HttpSession> sessions = this.getSessions(user.getEmail());

        int size = sessions.size();
        for (int i = 0; i < size; i++) {
            HttpSession s = sessions.get(0);
            this.remove(user.getEmail(), s);
        }

    }

    public void processAuthorizationChange(ValueChangeEvent event) {
        if (event.getNewValue() != null) {
            LOG.log(Level.WARNING, "the value has changed from {0} to {1}",
                    new Object[]{event.
                                getOldValue(), event.getNewValue()});
        }
    }

    public boolean isAuthorized(UserAccount user) {
        return Optional.ofNullable(userGroupFacade.find(user.getCltype()))
                .map(grp -> true)
                .orElse(false);
    }
}
