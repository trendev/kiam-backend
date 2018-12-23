/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fish.payara.cluster.Clustered;
import fish.payara.cluster.DistributedLockType;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * @author jsie
 */
@Clustered(callPostConstructOnAttach = false, callPreDestoyOnDetach = false,
        lock = DistributedLockType.LOCK, keyName = "white-map")
@Singleton
@Startup
public class JWTWhiteMap implements Serializable {

    private final Map<String, Set<JWTRecord>> map;

    private static final Logger LOG = Logger.getLogger(JWTWhiteMap.class.
            getName());

    public JWTWhiteMap() {
        this.map = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    @PostConstruct
    public void init() {
        //TODO: load the map from a DB
        LOG.
                log(Level.INFO, "{0} initialized", JWTWhiteMap.class.
                        getSimpleName());
    }

    @PreDestroy
    public void close() {
        //TODO : save the map in a DB and ignore if the map is empty (after test)
        LOG.log(Level.INFO, "{0} closed", JWTWhiteMap.class.getSimpleName());
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
        LOG.info("JWT White Map cleared");
    }

    /**
     * Cleans the map removing expired tokens and entries
     */
    public void clean() {
        for (Map.Entry<String, Set<JWTRecord>> e : this.map.entrySet()) {
            Set<JWTRecord> records = Optional.ofNullable(e.getValue())
                    .orElseGet(Collections::emptySet);

            records.removeIf(r -> {
                if (r.hasExpired()) {
                    LOG.log(Level.INFO,
                            "Token of user [{0}] ({1}) has expired and have been cleaned...",
                            new Object[]{
                                e.getKey(),
                                JWTManager.trunkToken(r.getToken())
                            });
                    return true;
                } else {
                    return false;
                }
            });
        }

        this.map.entrySet().removeIf(e -> {
            if (e.getValue().isEmpty()) {
                LOG.log(Level.INFO,
                        "All JWT Record of user [{0}] cleaned : no more entry in the JWT White Map (LOG-OUT)",
                        new Object[]{e.getKey()});
                return true;
            } else {
                return false;
            }
        });

    }

    /**
     * Adds a record for the provided email. if the user is not already
     * authenticated, its records set is initialized with a synchronized sorted
     * set.
     *
     * @param email the user's email
     * @param record the JWT Record
     * @return an Optional with the updated Set if "email" is already logged-in,
     * an empty Optional otherwise
     */
    public Optional<Set<JWTRecord>> add(String email, JWTRecord record) {
        Set<JWTRecord> records = this.map.getOrDefault(email,
                Collections.synchronizedSortedSet(new TreeSet<>()));
        records.add(record);

        //logged-in, first active "session"
        if (records.size() == 1) {
            LOG.log(Level.INFO,
                    "First JWT Record ({0}) added for user [{1}] in the JWT White Map (LOG-IN)",
                    new Object[]{
                        JWTManager.trunkToken(record.getToken()),
                        email
                    });
        } else {
            LOG.log(Level.INFO,
                    "JWT Record ({0}) added for user [{1}] in the JWT White Map : {2}",
                    new Object[]{
                        JWTManager.trunkToken(record.getToken()),
                        email,
                        records.size()
                    });
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

    /**
     * Removes the user "email" and its records from the JWT White Map
     *
     * @param email the email of the authenticated user
     * @return an Option with the record set if the user is authenticated, an
     * empty Optional otherwise
     */
    public Optional<Set<JWTRecord>> removeAll(String email) {
        Optional<Set<JWTRecord>> records =
                Optional.ofNullable(this.map.remove(email));

        if (records.isPresent()) {
            LOG.log(Level.INFO,
                    "All JWT Records of user [{0}] have been removed : no more entry in the JWT White Map (LOG-OUT)",
                    new Object[]{email});
        }
        return records;
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
                LOG.log(Level.INFO,
                        "Token of user [{0}] ({1}) removed from JWT White Map : {2}",
                        new Object[]{
                            email,
                            JWTManager.trunkToken(r.getToken()),
                            records.size()
                        });
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

    @Override
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
        String value = "NO_VALUE";
        try {
            value = om.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this.map);
        } catch (JsonProcessingException ex) {
            LOG.log(Level.SEVERE, "Impossible to display WhiteMap", ex);
        }
        return value;
    }

}
