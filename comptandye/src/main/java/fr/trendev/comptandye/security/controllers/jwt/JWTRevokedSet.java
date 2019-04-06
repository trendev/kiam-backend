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

    /**
     * SET MUST HAVE ONE MAIN COPY
     */
    private static volatile Set<JWTRecord> SET =
            Collections.synchronizedSortedSet(new TreeSet<>());

    private static final Logger LOG = Logger.getLogger(JWTRevokedSet.class.
            getName());

    @Inject
    JWTRevokedSetDTO dto;

    public JWTRevokedSet() {
    }

    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "{0} initializing", JWTRevokedSet.class.
                getSimpleName());

        this.dto.getAll()
                //restore the SET from a Collection
                .thenAccept(saved -> {
                    if (saved != null && !saved.isEmpty()) {
                        LOG.log(Level.INFO, "Restoring {0} from {1}",
                                new Object[]{JWTRevokedSet.class.getSimpleName(),
                                    this.dto.getClass().getSimpleName()});
                        SET.addAll(saved);
                        LOG.log(Level.INFO, "{0} revoked JWT restored in {1}",
                                new Object[]{saved.size(),
                                    JWTRevokedSet.class.getSimpleName()});
                    } else {
                        LOG.warning("No Revoked JWT found in Firestore...");
                    }
                });

        LOG.log(Level.INFO, "{0} may be initialized : revoked tokens = {1}",
                new Object[]{
                    JWTRevokedSet.class.getSimpleName(),
                    SET.size()});
    }

    @PreDestroy
    public void close() {
        LOG.log(Level.INFO, "{0} closed", JWTRevokedSet.class.getSimpleName());
    }

    public Set<JWTRecord> getSet() {
        return SET;
    }

    public void clear() {
        SET.clear();
        LOG.info("JWT Revoked Set cleared");
    }

    @Schedules({
        @Schedule(second = "*/5", minute = "*", hour = "*", persistent = false)
    })
    public void cleanUp() {
        SET.removeIf(r -> {
            if (r.hasExpired()) {
                LOG.log(Level.INFO,
                        "Revoked Token ({0}) has expired and has been cleaned...",
                        new Object[]{
                            JWTManager.trunkToken(r.getToken())
                        });

                dto.delete(r.getToken());
                return true;
            } else {
                return false;
            }
        });
    }

    public boolean add(JWTRecord record) {
        boolean result = SET.add(record);

        if (result) {
            this.dto.create(record);
        }

        return result;
    }

    /**
     * Used when multiple records are revoked (ex : force a user to logout from
     * all devices).
     *
     * @param records the JWT records to add
     * @return if the operation is successful, true, or otherwise, false
     */
    public boolean addAll(Set<JWTRecord> records) {
        boolean result = SET.addAll(records);

        if (result) {
            this.dto.bulkCreation(records);
        }

        return result;
    }

    public boolean contains(String token) {
        return SET.stream()
                .anyMatch(r -> r.getToken().equals(token));
    }

    public Optional<JWTRecord> remove(String token) {

        Optional<JWTRecord> record = SET.stream()
                .filter(r -> r.getToken().equals(token))
                .findFirst();

        record.ifPresent(r -> {
            if (SET.remove(r)) {
                this.dto.delete(r.getToken());
            }
        });

        return record;
    }

    public Optional<JWTRecord> remove(JWTRecord record) {
        boolean result = SET.remove(record);

        if (result) {
            this.dto.delete(record.getToken());
        }

        return result
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
                    .writeValueAsString(SET);
        } catch (JsonProcessingException ex) {
            LOG.log(Level.SEVERE, "Impossible to display RevokedSet", ex);
        }
        return value;
    }

}
