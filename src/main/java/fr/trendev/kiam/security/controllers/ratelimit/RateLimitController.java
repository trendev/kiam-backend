/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.ratelimit;

import fish.payara.cluster.Clustered;
import fish.payara.cluster.DistributedLockType;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 *
 * @author jsie
 */
@Clustered(callPostConstructOnAttach = false, callPreDestoyOnDetach = false,
        lock = DistributedLockType.LOCK, keyName = "access-records-cache")
@Singleton
@Startup
public class RateLimitController implements Serializable {
    
    private static final Logger LOG = Logger.getLogger(RateLimitController.class.getName());
    
    private final String className = RateLimitController.class.getSimpleName();
    
    private static volatile Map<String, Integer> PATH_LIMITATIONS
            = Collections.synchronizedSortedMap(new TreeMap<>());
    
    private static volatile Map<String, Map<String, List<Date>>> ACCESS_RECORDS
            = Collections.synchronizedSortedMap(new TreeMap<>());
    
    private static volatile int defaultLimit;
    
    private static final int NO_LIMIT = -1;
    
    public RateLimitController() {
    }
    
    @PostConstruct
    public void init() {
        try {
            this.loadLimitations();
            LOG.log(Level.INFO, "{0} : initialized", className);
            this.logPathLimitations();
        } catch (FileNotFoundException ex) {
            LOG.log(Level.SEVERE, "Error initializing " + className, ex);
        }
        
    }
    
    @PreDestroy
    public void destroy() {
        PATH_LIMITATIONS.clear();
        LOG.log(Level.INFO, "{0} : destroyed", className);
    }
    
    private void loadLimitations() throws FileNotFoundException {
        JsonReader reader = Json.createReader(
                Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream("json/path_limitations.json"));
        
        JsonObject config = reader.readObject();
        
        defaultLimit = config.getInt("default");
        
        JsonArray ja = config.getJsonArray("limitations");
        
        PATH_LIMITATIONS.clear();
        ja.forEach(jv -> {
            JsonObject jo = jv.asJsonObject();
            PATH_LIMITATIONS.put(
                    jo.getString("path"),
                    jo.getInt("limit"));
        });
    }
    
    private void logPathLimitations() {
        LOG.log(Level.INFO, "## DEFAULT LIMIT = {0}", defaultLimit);
        PATH_LIMITATIONS.forEach(
                (path, limit) -> LOG.log(Level.INFO,
                        "## Path : {0} | Limit : {1} ##",
                        new Object[]{path, limit})
        );
    }
    
    private Optional<Integer> controlPath(String path) {
        return PATH_LIMITATIONS.containsKey(path)
                ? Optional.of(PATH_LIMITATIONS.get(path))
                : Optional.empty();
    }
    
    private Optional<Map<String, List<Date>>> controlAccess(String address,
            String path,
            int limit) {
        if (limit == NO_LIMIT) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(ACCESS_RECORDS.get(address))
                    .map(
                            records -> Optional.ofNullable(records.get(path))
                                    .map(list -> {
                                        if (list.size() < limit) {
                                            list.add(new Date());
                                            return Optional.<Map<String, List<Date>>>empty();
                                        } else {
                                            return Optional.of(records);
                                        }
                                    })
                                    .orElseGet(() -> {
                                        List<Date> list = new LinkedList<>();
                                        list.add(new Date());
                                        records.put(path, list);
                                        return Optional.<Map<String, List<Date>>>empty();
                                    })
                    )
                    .orElseGet(() -> {
                        List<Date> list = new LinkedList<>();
                        list.add(new Date());
                        Map<String, List<Date>> map = new TreeMap<>();
                        map.put(path, list);
                        ACCESS_RECORDS.put(address, map);
                        return Optional.<Map<String, List<Date>>>empty();
                    });
        }
    }
    
    public Optional<Map<String, List<Date>>> control(String address, String path) {
        return this.controlPath(path)
                .map(limit -> this.controlAccess(address, path, limit))
                .orElseGet(() -> this.controlAccess(address, path, defaultLimit));
    }
    
    @Schedules({
        @Schedule(minute = "*", hour = "*")
    })
    void reset() {
        ACCESS_RECORDS.clear();
    }
    
}
