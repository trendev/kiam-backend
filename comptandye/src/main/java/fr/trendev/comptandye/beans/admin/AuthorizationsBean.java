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
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
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

    private final Logger LOG = Logger.getLogger(AuthorizationsBean.class.
            getName());

    @Inject
    private AuthorizationBeanProxy proxy;

    private List<Administrator> administrators;

    private List<Individual> individuals;

    private List<Professional> professionals;

    @PostConstruct
    public void init() {
        this.administrators = proxy.getAdministrators();
        this.individuals = proxy.getIndividuals();
        this.professionals = proxy.getProfessionals();
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

    public void processAuthorizationChange(UserAccount user) {
        proxy.processAuthorizationChange(user);
        this.disconnect(user);
        LOG.log(Level.INFO, "[{0}] is now {1}", new Object[]{
            user.getEmail(), user.isBlocked() ? "BLOCKED" : "GRANTED"
        });
    }

    public long getLastAccessedTime(UserAccount user) {
        long time = user.getLastAccessedTime();

        try {
            long max = time;
            List<HttpSession> sessions = this.getSessions(user.getEmail());
            for (int i = 0; i < sessions.size(); i++) {
                try {
                    max = (sessions.get(i).getLastAccessedTime() > max) ? sessions.
                            get(i).getLastAccessedTime() : max;
                } catch (IllegalStateException ex) {
                }
            }
            time = max;

        } catch (ConcurrentModificationException ex) {
            time = user.getLastAccessedTime();
        }

        return time;
    }

}
