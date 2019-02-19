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
import fr.trendev.comptandye.security.controllers.jwt.dto.JWTRevokedSetDTO;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
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
        lock = DistributedLockType.LOCK, keyName = "revoked-set")
@Singleton
@Startup
public class JWTRevokedSet implements Serializable {

    private final Set<JWTRecord> set;

    private static final Logger LOG = Logger.getLogger(JWTRevokedSet.class.
            getName());

    @Inject
    JWTRevokedSetDTO dto;

    public JWTRevokedSet() {
        this.set = Collections.synchronizedSortedSet(new TreeSet<>());
    }

    @PostConstruct
    public void init() {
        //TODO: load the set from a DB
        LOG.log(Level.INFO, "{0} initialized", JWTRevokedSet.class.
                getSimpleName());
    }

    @PreDestroy
    public void close() {
        //TODO : save the set in a DB and ignore if the set is empty (after test)
        LOG.log(Level.INFO, "{0} closed", JWTRevokedSet.class.getSimpleName());
    }

    public Set<JWTRecord> getSet() {
        return this.set;
    }

    public void clear() {
        this.set.clear();
        LOG.info("JWT Revoked Set cleared");
    }

    @Schedules({
        @Schedule(minute = "*", hour = "*", persistent = false)
    })
    public void cleanUp() {
        this.set.removeIf(r -> {
            if (r.hasExpired()) {
                LOG.log(Level.INFO,
                        "Revoked Token ({0}) has expired and has been cleaned...",
                        new Object[]{
                            JWTManager.trunkToken(r.getToken())
                        });
                return true;
            } else {
                return false;
            }
        });
    }

    public boolean add(JWTRecord record) {
        return this.set.add(record);
    }

    public boolean addAll(Collection<JWTRecord> records) {
        return this.set.addAll(records);
    }

    public boolean contains(String token) {
        return this.set.stream()
                .anyMatch(r -> r.getToken().equals(token));
    }

    public Optional<JWTRecord> remove(String token) {

        Optional<JWTRecord> record = this.set.stream()
                .filter(r -> r.getToken().equals(token))
                .findFirst();

        record.ifPresent(r -> this.set.remove(r));

        return record;
    }

    public Optional<JWTRecord> remove(JWTRecord record) {
        return this.set.remove(record)
                ? Optional.of(record)
                : Optional.empty();
    }

    @Override
    public String toString() {
        ObjectMapper om = new ObjectMapper();
        om.setDateFormat(new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"));
        String value = "NO_VALUE";
        try {
            value = om.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this.set);
        } catch (JsonProcessingException ex) {
            LOG.log(Level.SEVERE, "Impossible to display RevokedSet", ex);
        }
        return value;
    }

}
