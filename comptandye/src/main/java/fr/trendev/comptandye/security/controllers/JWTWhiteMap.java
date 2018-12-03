/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author jsie
 */
//@Clustered(callPostConstructOnAttach = false, callPreDestoyOnDetach = false,
//        lock = DistributedLockType.LOCK, keyName = "white-map")
@Singleton
@Startup
public class JWTWhiteMap implements Serializable {

    private final Map<String, Set<JWTRecord>> map;

    private static final Logger LOG = Logger.getLogger(JWTWhiteMap.class.
            getName());

    transient private Timer timer;

    public JWTWhiteMap() {
        this.map = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    private Timer getTimer() {
        if (this.timer == null) {
            this.timer = new Timer();
        }
        return this.timer;
    }

    @PostConstruct
    public void init() {
        //TODO: load the map from a DB
        LOG.log(Level.INFO, "{0} initialized", JWTWhiteMap.class.getName());
    }

    @PreDestroy
    public void close() {
        //TODO : save the map in a DB and ignore if the map is empty (after test)
        this.getTimer().cancel();
        LOG.log(Level.INFO, "{0} closed", JWTWhiteMap.class.getName());
    }

    /**
     * Returns the map itself, not an unmodifiable view
     *
     * @return the map
     */
    public Map<String, Set<JWTRecord>> getMap() {
        return this.map;
    }

    /**
     * Clears the map
     */
    public void clear() {
        this.map.clear();
        LOG.info("JWT White Map cleaned");
    }

    /**
     * Adds a record for the provided email
     *
     * @param email the user's email
     * @param record the JWT Record
     * @return an Optional with the updated Set if "email" is already logged-in,
     * an empty Optional otherwise
     */
    public Optional<Set<JWTRecord>> add(String email, JWTRecord record) {
        Set<JWTRecord> records = this.map.getOrDefault(email, new TreeSet<>());
        records.add(record);

        this.getTimer().schedule(new TimerTask() {
            @Override
            public void run() {
                remove(email, record.getToken());
            }
        }, record.getExpirationDate());

        //logged-in, first active "session"
        if (records.size() == 1) {
            LOG.log(Level.INFO,
                    "First JWT Record added for user [{0}] in the JWT White Map (LOG-IN)",
                    new Object[]{email});
        }

        return Optional.ofNullable(this.map.put(email, records));
    }

    /**
     * Returns the records
     *
     * @param email the authenticated (or not) user
     * @return an Option with the record set if the user is authenticated, an
     * empty Optional otherwise
     */
    public Optional<Set<JWTRecord>> getRecords(String email) {
        return Optional.ofNullable(this.map.get(email));
    }

    private void displayMap() {
        this.map.entrySet().
                forEach((e) -> {
                    e.getValue().
                            forEach((r) -> {
                                LOG.log(Level.WARNING,
                                        "Thread '{'{0}'}' ENTRY : [{1}] / {2}",
                                        new Object[]{
                                            Thread.currentThread().getName(),
                                            e.getKey(),
                                            this.trunkToken(r.getToken())});
                            });
                });
    }

    /**
     * Searches and removes (if present) a JWT Record from the records and
     * removes the authenticated user from the map if there is no entry anymore
     * (cleaning)
     *
     * @param email the email of the authenticated user
     * @param token the JWT
     * @param records the JWT records of the authenticated user
     * @return an Optional with the JWT Record if the token is found or an empty
     * Optional
     */
    private Optional<JWTRecord> remove(String email,
            String token,
            Set<JWTRecord> records) {
        Optional<JWTRecord> record = records.stream()
                .filter(r -> r.getToken().equals(token))
                .findFirst();//should be unique

        record.ifPresent(r -> {
            if (records.remove(r)) {
                LOG.log(Level.INFO, "Token of user ["
                        + email + "] ("
                        + this.trunkToken(r.getToken())
                        + ") removed from JWT White Map");
            }
        });

        // logged-out, no more active "session"
        if (records.isEmpty() && record.isPresent()) {
            this.map.remove(email);
            LOG.log(Level.INFO,
                    "Last JWT Record of user [{0}] removed : no more entry in the JWT White Map (LOG-OUT)",
                    new Object[]{email});
        }

        return record;
    }

    private String trunkToken(String token) {
        int l = token.length();
        int n = 10;
        return l < n ? token : "..." + token.substring(l - n, l);
    }

    /**
     * Removes the JWT record directly from the JWT record set associated to an
     * authenticated user. <\br>
     * Performance = o(log n + log r)
     *
     * @param email the email of the authenticated user
     * @param token the JWT
     * @return
     */
    public Optional<JWTRecord> remove(String email, String token) {
        displayMap();
        return this.remove(email,
                token,
                this.map.getOrDefault(email, new TreeSet<>()));
    }

    /**
     * Searches and removes a token in the entire map.  <\br>
     * Performance = o(n + log r)
     *
     * @param token
     * @return
     */
    public Optional<JWTRecord> remove(String token) {
        for (Map.Entry<String, Set<JWTRecord>> e : this.map.entrySet()) {
            Optional<JWTRecord> record = this.remove(e.getKey(),
                    token,
                    e.getValue());
            if (record.isPresent()) {
                return record;
            }
        }
        return Optional.empty();
    }

}
