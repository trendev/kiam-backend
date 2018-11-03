/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.weblisteners;

import fr.trendev.comptandye.utils.security.ActiveSessionTracker;
import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 * Tracks HTTP Sessions lifecycle changes
 *
 * @author jsie
 */
@WebListener
public class WebAppSessionListener implements HttpSessionListener,
        HttpSessionIdListener {

    @Inject
    private ActiveSessionTracker tracker;

    private static final Logger LOG = Logger.getLogger(
            WebAppSessionListener.class.getName());

    private static final DateFormat df = DateFormat.getDateTimeInstance(
            DateFormat.FULL, DateFormat.FULL);

    /**
     * Adds the session in the tracker. No user provided (use empty string).
     *
     * @param se the creation event
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession hs = se.getSession();
        tracker.insert(hs.getId(), "");
        LOG.log(Level.INFO, "Session [{0}] has been created on {1}",
                new Object[]{
                    hs.getId(),
                    df.format(new Date(hs.getCreationTime()))
                });
    }

    /**
     * Removes the session from the tracker's index. If the listener is the
     * origin of the session destruction, an object is returned from the
     * previous removal. The listener then call the remove method of the
     * tracker.
     *
     * @see ActiveSessionTracker#remove(java.lang.String,
     * javax.servlet.http.HttpSession)
     *
     * @param se the destruction event
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession hs = se.getSession();

        String email = tracker.drop(hs.getId());
        if (email != null && !email.isEmpty()) {
            tracker.remove(email, hs);
        }

        LOG.log(Level.INFO, "Session [{0}] has been destroyed on {1}",
                new Object[]{
                    hs.getId(),
                    df.format(new Date())
                });
    }

    /**
     * Drops the session id from the tracker's index and inserts it with the
     * association user's email (or an empty string) in the tracker.
     *
     * @param se the change event
     * @param oldSessionId the old session id
     */
    @Override
    public void sessionIdChanged(HttpSessionEvent se, String oldSessionId) {
        HttpSession hs = se.getSession();
        String email = tracker.drop(oldSessionId);
        tracker.insert(hs.getId(), email);
        LOG.log(Level.INFO,
                "Session [{0}] has been changed to [{1}] and email [{2}] has been inserted in the tracker index on {3}",
                new Object[]{
                    oldSessionId,
                    hs.getId(),
                    email,
                    df.format(new Date())
                });
    }

}
