/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class ActiveSessionTracker {

    private final Map<String, List<HttpSession>> map;
    private static final Logger LOG = Logger.getLogger(
            ActiveSessionTracker.class.getName());

    public ActiveSessionTracker() {
        map = new ConcurrentHashMap<>();
    }

    @PostConstruct
    private void init() {
        LOG.log(Level.INFO, "{0} initialized",
                ActiveSessionTracker.class.getSimpleName());
    }

    /**
     * Checks if there is still active session in the tracker before its
     * destruction, just for logging purposes.
     */
    @PreDestroy
    private void destroy() {
        LOG.log(Level.INFO, "{0} will be destroyed",
                ActiveSessionTracker.class.getSimpleName());

        if (map.isEmpty()) {
            LOG.log(Level.INFO,
                    "NO ACTIVE SESSION in {0}",
                    ActiveSessionTracker.class.getSimpleName());
        } else {
            LOG.log(Level.WARNING, "{0} still contains active sessions !!!",
                    ActiveSessionTracker.class.getSimpleName());

            map.entrySet().forEach(e -> {
                LOG.log(Level.WARNING, "{0}: {1} session{2}", new Object[]{
                    e.
                    getKey(),
                    e.getValue().size(), e.getValue().size() > 1 ? "s" : ""});
            });
        }
    }

    /**
     * Returns true if there is no active session.
     *
     * @return true if the tracker is empty, false otherwise
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Associates a session with a logged-in user in the tracker.
     *
     * @param email the user associated to the session
     * @param session the session to add
     */
    public void put(String email, HttpSession session) {
        map.computeIfAbsent(email, k -> new LinkedList<>());
        map.get(email).add(session);
    }

    public boolean contains(String email, HttpSession session) {
        return Optional.ofNullable(map.get(email))
                .map(s -> s.contains(session))
                .orElse(false);
    }

    /**
     * Removes and invalidates a session for a logged in user. Removes the user
     * from the tracker when no session. Used for logout process.
     *
     * @param email the user to disconnect
     * @param session the session to remove/invalidate
     * @return true if session has been removed false otherwise
     */
    public boolean remove(String email, HttpSession session) {

        return Optional.ofNullable(map.get(email))
                .map(s -> {
                    String id = session.getId();
                    LOG.log(Level.INFO,
                            "Removing session {0} associated with user [{1}]",
                            new Object[]{id,
                                email});

                    boolean result = s.remove(session);
                    session.invalidate();
                    LOG.log(Level.INFO,
                            "Session {0} removed from {1} and invalidated",
                            new Object[]{id,
                                ActiveSessionTracker.class.getSimpleName()});

                    if (s.isEmpty()) {
                        map.remove(email);
                        LOG.log(Level.INFO,
                                "user [{0}] has no active session and is now removed from {1}",
                                new Object[]{email,
                                    ActiveSessionTracker.class.getSimpleName()});
                    }

                    return result;
                })
                .orElse(false);

    }

    /**
     * Counts the number of session associated to a specific logged-in user.
     *
     * @param name the logged-in user email
     * @return the number of session, returns 0 if the user is not a logged-in
     * user
     */
    public int count(String name) {
        return Optional.ofNullable(map.get(name))
                .map(List::size)
                .orElse(0);
    }

    /**
     * Returns the active session List for a specific user
     *
     * @param email the user's email
     * @return the active session list or an empty list if the user is not
     * logged-in
     */
    public List<HttpSession> getSession(String email) {
        return Optional.ofNullable(map.get(email))
                .map(Function.identity())
                .orElse(Collections.emptyList());
    }

    public List<String> getLoggedUsers() {
        return new ArrayList<>(map.keySet());
    }

}