/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fish.payara.cluster.Clustered;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.util.Collections;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
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
public class JWTRevokedSet {

    private Set<JWTRecord> set;

    private static final Logger LOG = Logger.getLogger(JWTRevokedSet.class.
            getName());

    public JWTRevokedSet() {
        this.set = Collections.synchronizedSortedSet(new TreeSet<>());
    }

    @PostConstruct
    public void init() {
        //TODO: load the set from a DB
        LOG.log(Level.INFO, "{0} initialized", JWTRevokedSet.class.getName());
    }

    @PreDestroy
    public void close() {
        //TODO : save the set in a DB
        LOG.log(Level.INFO, "{0} closed", JWTRevokedSet.class.getName());
    }

    public Set<JWTRecord> getSet() {
        return this.set;
    }

    public void clear() {
        this.set.clear();
        LOG.info("JWT Revoked Set cleaned");
    }

    public boolean add(JWTRecord record) {
        return this.set.add(record);
    }
    
    public boolean addAll(Collection<JWTRecord> records){
        return this.set.addAll(records);
    }

    //TODO : to implement/test
    public boolean contains(String token) {
        return false;
    }

    //TODO : to implement/test
    public Optional<JWTRecord> remove(String token) {
        return null;
    }

    //TODO : to implement/test
    public Optional<JWTRecord> remove(JWTRecord record) {
        return null;
    }

}
