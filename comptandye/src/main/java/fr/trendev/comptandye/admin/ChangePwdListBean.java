/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.admin;

import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.sessions.AdministratorFacade;
import fr.trendev.comptandye.sessions.IndividualFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.UserAccountFacade;
import fr.trendev.comptandye.security.controllers.PasswordGenerator;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class ChangePwdListBean implements Serializable {

    private final Logger LOG = Logger.getLogger(ChangePwdListBean.class.
            getName());

    @Inject
    private PasswordGenerator passwordGenerator;

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

    private String password;

    private UserAccount user;

    public List<Administrator> getAdministrators() {
        return administrators;
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public List<Professional> getProfessionals() {
        return professionals;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        LOG.log(Level.WARNING, "setPassword = {0}", password);
        this.password = password;
    }

    public UserAccount getUser() {
        return user;
    }

    public void setUser(UserAccount user) {
        this.user = user;
    }

    @PostConstruct
    public void init() {
        this.administrators = administratorFacade.findAll();
        this.individuals = individualFacade.findAll();
        this.professionals = professionalFacade.findAll();
    }

    public void clear() {
        this.password = "";
    }

    public String encryptPwd() {
        String pwd = this.password;
        LOG.log(Level.WARNING, "password = {0}", pwd);
        LOG.log(Level.WARNING, "Will update {0}", user.getEmail());
        String epwd = passwordGenerator.encrypt_SHA256(pwd);
        user.setPassword(epwd);
        userAccountFacade.edit(user);
        this.clear();
        this.init();
        return "change-pwd-list";
    }

    public String npwd(UserAccount user) {
        this.user = user;
        return "change-pwd";
    }

    public void generate() {
        this.password = passwordGenerator.autoGenerate();
    }

}
