/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans.admin;

import fr.trendev.comptandye.utils.security.ActiveSessionTracker;
import fr.trendev.comptandye.entities.UserAccount;
import fr.trendev.comptandye.sessions.UserAccountFacade;
import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
public abstract class CommonUsersBean implements Serializable {

    @Inject
    private UserAccountFacade userAccountFacade;

    @Inject
    private ActiveSessionTracker tracker;

    /**
     * The datamodel, a flat snapshot of the tracker
     */
    private List<LoggedInUser> loggedInUsers;

    /**
     * The session timeout in seconds
     */
    private int session_timeout;

    /**
     * The duration of a track record, usually 1 minute
     */
    private int d;

    /**
     * The duration of a track record in milliseconds
     */
    private long duration;

    /**
     * The number of track records / stripes
     */
    private int stripes;

    /**
     * Refresh delay
     */
    private int refresh = 2;

    public UserAccountFacade getUserAccountFacade() {
        return userAccountFacade;
    }

    public int getSession_timeout() {
        return session_timeout;
    }

    public int getD() {
        return d;
    }

    public long getDuration() {
        return duration;
    }

    public int getStripes() {
        return stripes;
    }

    public int getRefresh() {
        return refresh;
    }

    /**
     * Computes and sets the timeout, durations and stripes and initializes
     * (loads) the data model.
     */
    @PostConstruct
    public void init() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) fc.getExternalContext().
                getSession(false);

        this.session_timeout = session.getMaxInactiveInterval();
        this.d = 1;
        this.duration = d * 60 * 1000l;
        this.stripes = (this.session_timeout / 60) / d;

        this.initLoggedInUsers();
    }

    /**
     * Loads/Refreshes the data model. Filters and ignores the sessions which
     * will be invalidated during the refreshed JSF session.
     */
    public void initLoggedInUsers() {
        long now = System.currentTimeMillis();

        //sessions older than overdue will be ignored
        long overdue = now - (session_timeout * 1000l) + (this.refresh
                * 1000l);

        try {
            List<LoggedInUser> list = new LinkedList<>();
            this.getUserAccounts().forEach(u -> this.getSessions(u).forEach(
                    s -> {
                try {
                    //ignore session which will be invalidated after this refresh
                    if (this.getMostRecentAccessTime(s) > overdue) {
                        list.add(
                                new LoggedInUser(u, this.
                                        getTypeOfUser(u), s));
                    }
                } catch (IllegalStateException ex) {
                    //ignores invalidated sessions
                }
            }));
            //updates the logged in user list
            this.loggedInUsers = list;
        } catch (ConcurrentModificationException ex) {
            //cannot update the model because the tracker is locked
            //uses the previous version of the data model instead 
            //but cleans the potential invalidated session
            this.loggedInUsers = this.loggedInUsers.stream()
                    .filter(u -> {
                        boolean validity = false;
                        try {
                            validity = this.getMostRecentAccessTime(u.
                                    getHttpSession())
                                    > overdue;
                        } catch (IllegalStateException ise) {
                            //ignores invalidated session
                        }
                        return validity;
                    })
                    .collect(Collectors.toList());
        }
    }

    /**
     * Gets the emails of the logged-in users
     *
     * @return the emails of the users
     */
    public List<String> getUserAccounts() {
        List<String> users = tracker.getLoggedUsers();
        users.sort(String::compareTo);
        return users;
    }

    /**
     * Returns the sessions of the specified user.
     *
     * @param email the user's email
     * @return the http session list
     */
    protected List<HttpSession> getSessions(String email) {
        List<HttpSession> sessions = tracker.getSession(email);
        sessions.sort((s1, s2) -> s1.getId().compareTo(s2.getId()));
        return sessions;
    }

    /**
     * Removes an active session linked for a specific user and update the data
     * model used in the form.
     *
     * @param email the user's email
     * @param session the http session to remove from the tracker and invalidate
     * @return the result of the remove from the tracker
     */
    public boolean remove(String email, HttpSession session) {
        boolean result = tracker.remove(email, session);
        this.initLoggedInUsers();
        return result;
    }

    protected String getTypeOfUser(String email) {
        return userAccountFacade.getUserAccountType(email);
    }

    /**
     * Returns the datamodel (a flat snapshot of the tracker)
     *
     * @return the datamodel, the list of the logged-in user
     */
    public List<LoggedInUser> getLoggedInUsers() {
        return loggedInUsers;
    }

    /**
     * Provides the last access time. Can be the disconnection/logout timestamp,
     * the most recent lastAccessedTime value of its HttpSessions or the most
     * recent timestamp of its last Http request (using the RQT_TIMESTAMP
     * attribute added in the session by {@link OverallFilter}.
     *
     * @param user the user whose the accessed time is provided
     * @return the last access time, RQT_TIMESTAMP is the usually favored
     * option.
     */
    public long getLastAccessedTime(UserAccount user) {

        try {
            long max = user.getLastAccessedTime();
            List<HttpSession> sessions = this.getSessions(user.getEmail());
            for (int i = 0; i < sessions.size(); i++) {
                try {
                    long mrat = this.getMostRecentAccessTime(sessions.get(i));
                    max = mrat > max ? mrat : max;

                } catch (IllegalStateException ex) {
                }
            }

            return max;

        } catch (ConcurrentModificationException ex) {
            return user.getLastAccessedTime();
        }

    }

    public long getMostRecentAccessTime(HttpSession session) throws
            IllegalStateException {
        Long rqt_timestamp = (Long) session.getAttribute("RQT_TIMESTAMP");
        long time;

        if (rqt_timestamp != null && rqt_timestamp > session.
                getLastAccessedTime()) {
            time = rqt_timestamp;
        } else {
            time = session.getLastAccessedTime();
        }

        return time;
    }
}
