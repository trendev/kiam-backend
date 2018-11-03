/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.admin;

import fr.trendev.comptandye.security.controllers.ActiveSessionTracker;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.sessions.AdministratorFacade;
import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Named
@SessionScoped
public class AdminBean implements Serializable {

    @Inject
    private AdministratorFacade administratorFacade;

    @Inject
    private ActiveSessionTracker tracker;

    private String adminEmail;

    private final Logger LOG = Logger.
            getLogger(AdminBean.class.getName());

    public AdminBean() {
    }

    public Administrator getAdministrator() {
        return administratorFacade.find(adminEmail);
    }

    /**
     * Inits the bean. Set the user's email address of the current
     * Administrator.
     */
    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();

        if (fc == null) {
            String errmsg = "There is no FacesContext creating an AdminBean !";
            LOG.log(Level.SEVERE, errmsg);
            throw new IllegalStateException(errmsg);
        }

        HttpServletRequest req = (HttpServletRequest) fc.
                getExternalContext().getRequest();

        Principal user = req.getUserPrincipal();

        if (user == null) {
            String errmsg = "An Administrator has requested an AdminBean creation but this is not a logged-in user !";
            LOG.log(Level.SEVERE, errmsg);
            throw new IllegalStateException(errmsg);
        }

        this.adminEmail = user.getName();
    }

    /**
     * Logs-out the current Administrator.
     *
     * @see ActiveSessionTracker#remove(java.lang.String,
     * javax.servlet.http.HttpSession)
     * @throws IOException
     */
    public void logout() throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().
                getSession(false);
        LOG.log(Level.INFO,
                "Logout Administrator [{0}] from sesssion {1}",
                new Object[]{adminEmail,
                    session.getId()});

        tracker.remove(adminEmail, session);

        ExternalContext ec = FacesContext.getCurrentInstance().
                getExternalContext();
        ec.redirect(ec.getRequestContextPath());
    }

    /**
     * Returns the sessions associated to the current Administrator
     *
     * @return the session list
     */
    public List<HttpSession> getAdminSessions() {
        return tracker.getSession(adminEmail);
    }

}
