/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fish.payara.cluster.Clustered;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.Serializable;
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
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author jsie
 */
@Clustered
@ApplicationScoped
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
        LOG.log(Level.INFO, "{0} initialized", JWTWhiteMap.class.getName());
    }

    @PreDestroy
    public void close() {
        //TODO : save the map in a DB
        LOG.log(Level.INFO, "{0} closed", JWTWhiteMap.class.getName());
    }

    public Map<String, Set<JWTRecord>> getMap() {
        return this.map;
    }

    public void clear() {
        this.map.clear();
        LOG.info("JWT White Map cleaned");
    }

    public Optional<Set<JWTRecord>> add(String email, JWTRecord record) {
        Set<JWTRecord> records = this.map.getOrDefault(email, new TreeSet<>());
        records.add(record);

        //logged-in, first active "session"
        if (records.size() == 1) {
            LOG.log(Level.INFO,
                    "First JWT Record added for user [{0}] in the JWT White Map (LOG-IN)",
                    new Object[]{email});
        }

        return Optional.ofNullable(this.map.put(email, records));
    }

    public Optional<Set<JWTRecord>> getTokens(String email) {
        return Optional.ofNullable(this.map.get(email));
    }

    public Optional<JWTRecord> remove(String email, String token) {
        Set<JWTRecord> records = this.map.getOrDefault(email, new TreeSet<>());

        Optional<JWTRecord> record = records.stream()
                .filter(r -> r.getToken().equals(token))
                .findFirst();//should be unique

        record.ifPresent(r -> records.remove(r));

        // logged-out, no more active "session"
        if (records.isEmpty() && record.isPresent()) {
            this.map.remove(email);
            LOG.log(Level.INFO,
                    "Last JWT Record of user [{0}] removed : no more entry in the JWT White Map (LOG-OUT)",
                    new Object[]{email});
        }

        return record;
    }

    // TODO : explore the WhiteMap
    public Optional<JWTRecord> remove(String token) {
        return null;
    }

}
