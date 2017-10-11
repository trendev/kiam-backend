/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.listeners;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionIdListener;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author jsie
 */
public class WebAppSessionListener implements HttpSessionListener,
        HttpSessionIdListener {

    @Inject
    ActiveSessionTracker tracker;

    private static final Logger LOG = Logger.getLogger(
            WebAppSessionListener.class.getName());

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        HttpSession hs = se.getSession();
        tracker.insert(hs.getId(), "");
        LOG.log(Level.INFO, "Session [{0}] has been created on {1}",
                new Object[]{
                    hs.getId(),
                    new Date(hs.getCreationTime())
                });
    }

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
                    new Date()
                });
    }

    @Override
    public void sessionIdChanged(HttpSessionEvent se, String oldSessionId) {
        HttpSession hs = se.getSession();
        String email = tracker.drop(oldSessionId);
        tracker.insert(hs.getId(), email);
        LOG.log(Level.INFO,
                "Session [{0}] has been changed to [{1}] and email [{2}] has been inserted in the tracker index]",
                new Object[]{
                    oldSessionId,
                    hs.getId(),
                    email
                });
    }

}
