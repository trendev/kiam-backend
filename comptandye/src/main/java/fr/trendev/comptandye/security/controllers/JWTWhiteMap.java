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
        LOG.log(Level.INFO, JWTWhiteMap.class.getName() + " initialized");
    }

    @PreDestroy
    public void close() {
        //TODO : save the map in a DB
        LOG.log(Level.INFO, JWTWhiteMap.class.getName() + " closed");
    }

    public Map<String, Set<JWTRecord>> getMap() {
        return Collections.unmodifiableMap(map);
    }

    public void clear() {
        this.map.clear();
    }

    public Set<JWTRecord> add(String email, JWTRecord record) {
        Set<JWTRecord> records = map.getOrDefault(email, new TreeSet<>());
        records.add(record);

        Set<JWTRecord> upd8Records = map.put(email, records);

        return upd8Records == null ? null : Collections.unmodifiableSet(
                upd8Records);
    }

}
