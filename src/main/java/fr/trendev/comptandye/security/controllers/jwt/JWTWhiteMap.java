/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fish.payara.cluster.Clustered;
import fish.payara.cluster.DistributedLockType;
import fr.trendev.comptandye.security.controllers.AuthenticationEventController;
import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTRecord;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
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
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * @author jsie
 */
@Clustered(callPostConstructOnAttach = false, callPreDestoyOnDetach = false,
        lock = DistributedLockType.LOCK, keyName = "white-map")
@Singleton
@Startup
public class JWTWhiteMap implements Serializable {

    /**
     * MAP MUST HAVE ONE MAIN COPY
     */
    private static volatile Map<String, Set<JWTRecord>> MAP =
            Collections.synchronizedSortedMap(new TreeMap<>());

    private static final Logger LOG = Logger.getLogger(JWTWhiteMap.class.
            getName());

    @Inject
    JWTWhiteMapDTO dto;

    @Inject
    private AuthenticationEventController aec;

    public JWTWhiteMap() {
    }

    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "Initializing {0} ...",
                JWTWhiteMap.class.getSimpleName());

        dto.getAll()
                //restores the map
                .thenAccept(saved -> {
                    if (saved != null && !saved.isEmpty()) {
                        LOG.info("Restoring " + JWTWhiteMap.class.
                                getSimpleName()
                                + " from " + dto.getClass().getSimpleName()
                                + " and parsing " + saved.size() + " documents");
                        /**
                         * iterates over the DTO's list and merge the entries if
                         * there is already authenticated users in the
                         * jwtwhitemap. Should not happen... if it happens it
                         * means the API is operational before the DTO has
                         * provided the saved entries.
                         */
                        saved.forEach(e -> {
                            Set<JWTRecord> records = MAP.getOrDefault(
                                    e.getEmail(),
                                    Collections
                                            .synchronizedSortedSet(
                                                    new TreeSet<>()));

                            LOG.log(Level.INFO,
                                    "{0} records found for user [{1}]",
                                    new Object[]{e.getRecords().size(),
                                        e.getEmail()});

                            if (records.isEmpty()) {
                                LOG.info("Restoring JWT Records for user " + e.
                                        getEmail());
                            } else { // active authenticated user
                                LOG.warning("Updating JWT Records for user "
                                        + e.
                                                getEmail());
                            }

                            records.addAll(e.getRecords());
                            MAP.put(e.getEmail(), records);
                        });
                    } else {
                        LOG.warning("No JWTWhiteMap entries to restore !");
                    }
                });

        LOG.log(Level.INFO, "{0} may be initialized : active users = {1}",
                new Object[]{
                    JWTWhiteMap.class.getSimpleName(),
                    MAP.size()});

    }

    @PreDestroy
    public void close() {
        LOG.log(Level.INFO, "{0} closed", JWTWhiteMap.class.getSimpleName());
    }

    /**
     * Returns the map itself, not an unmodifiable view
     *
     * @return the map
     */
    public Map<String, Set<JWTRecord>> getMap() {
        return MAP;
    }

    /**
     * Clears the map. Use for test purposes only.
     */
    public void clear() {
        MAP.clear();
        LOG.info("JWT White Map cleared");
    }

    /**
     * Cleans the map removing expired tokens and entries
     */
    @Schedules({
        @Schedule(second = "*/10", minute = "*", hour = "*")
    })
    public void cleanUp() {

        MAP.entrySet().forEach(e -> {

            // get the JWTRecords associated with the email or provide an empty Set
            Set<JWTRecord> records = Optional.ofNullable(e.getValue())
                    .orElseGet(Collections::emptySet);

            boolean result = records.removeIf(r -> {
                if (r.hasExpired()) {
                    LOG.log(Level.INFO,
                            "Token of user [{0}] ({1}) has expired and has been cleaned...",
                            new Object[]{
                                e.getKey(),
                                JWTManager.trunkToken(r.getToken())
                            });
                    return true;
                } else {
                    return false;
                }
            });

            // some expired records have been found and removed from the Map (cache)
            if (result == true) {
                if (records.isEmpty()) { // no more entry, user is logged out
                    String email = e.getKey();
                    LOG.log(Level.INFO,
                            "All JWT Record of user [{0}] cleaned : no more entry in the JWT White Map (LOG-OUT)",
                            new Object[]{email});
                    dto.delete(email);
                    this.aec.logout(email); // emits a LOG-OUT event
                } else { // there is still some valid JWT in the records
                    dto.update(new JWTWhiteMapEntry(e));
                }
            }
        });

        /**
         * Records are cleaned during the first iteration. The Set of records
         * becoming empty, the associated entries must also be removed from the
         * Map
         */
        MAP.entrySet().removeIf(e -> e.getValue().isEmpty());
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
        Set<JWTRecord> records = MAP.getOrDefault(email,
                Collections.synchronizedSortedSet(new TreeSet<>()));

        records.add(record);

        //logged-in, first active "session"
        if (records.size() == 1) {

            this.dto.create(new JWTWhiteMapEntry(email, records));

            LOG.log(Level.INFO,
                    "First JWT Record ({0}) added for user [{1}] in the JWT White Map (LOG-IN)",
                    new Object[]{
                        JWTManager.trunkToken(record.getToken()),
                        email
                    });
            this.aec.login(email); // emits LOG-IN event
        } else {

            this.dto.update(new JWTWhiteMapEntry(email, records));

            LOG.log(Level.INFO,
                    "JWT Record ({0}) added for user [{1}] in the JWT White Map : {2}",
                    new Object[]{
                        JWTManager.trunkToken(record.getToken()),
                        email,
                        records.size()
                    });
        }

        return Optional.ofNullable(MAP.put(email, records));
    }

    /**
     * Returns the records
     *
     * @param email the authenticated (or not) user
     * @return an Option with the record set if the user is authenticated, an
     * empty Optional otherwise
     */
    public Optional<Set<JWTRecord>> getRecords(String email) {
        return Optional.ofNullable(MAP.get(email));
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
                Optional.ofNullable(MAP.remove(email));

        if (records.isPresent()) {
            LOG.log(Level.INFO,
                    "All JWT Records of user [{0}] have been removed : no more entry in the JWT White Map (LOG-OUT)",
                    new Object[]{email});
            this.dto.delete(email);
            this.aec.logout(email); // emits a LOG-OUT event
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

        if (record.isPresent()) {
            // logged-out, no more active "session"
            if (records.isEmpty()) {

                MAP.remove(email);
                this.dto.delete(email);

                LOG.log(Level.INFO,
                        "Last JWT Record of user [{0}] removed : no more entry in the JWT White Map (LOG-OUT)",
                        new Object[]{email});
                this.aec.logout(email); // emits a LOG-OUT event

            } else { // save the updated record collection
                this.dto.update(new JWTWhiteMapEntry(email, records));
            }
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
                MAP.getOrDefault(email, new TreeSet<>()));
    }

    /**
     * Searches and removes a token in the entire map.  <\br>
     * Performance = o(n + log r)
     *
     * @param token
     * @return
     */
    public Optional<JWTRecord> remove(String token) {
        for (Map.Entry<String, Set<JWTRecord>> e : MAP.entrySet()) {
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
                    .writeValueAsString(MAP);
        } catch (JsonProcessingException ex) {
            LOG.log(Level.SEVERE, "Impossible to display WhiteMap", ex);
        }
        return value;
    }

}