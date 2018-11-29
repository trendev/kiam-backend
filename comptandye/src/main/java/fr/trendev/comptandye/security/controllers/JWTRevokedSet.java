/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fish.payara.cluster.Clustered;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.util.Collections;
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

}
