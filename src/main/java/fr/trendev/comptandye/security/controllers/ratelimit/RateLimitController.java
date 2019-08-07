/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.ratelimit;

import fish.payara.cluster.Clustered;
import fish.payara.cluster.DistributedLockType;
import java.io.Serializable;
import java.util.Optional;
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
@Clustered(callPostConstructOnAttach = false, callPreDestoyOnDetach = false,
        lock = DistributedLockType.LOCK, keyName = "access-records-cache")
@Singleton
@Startup
public class RateLimitController implements Serializable {

    private static final Logger LOG = Logger.getLogger(RateLimitController.class.getName());

    private final String className = RateLimitController.class.getSimpleName();

    public RateLimitController() {
    }

    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "{0} : initialized", className);
    }

    @PreDestroy
    public void destroy() {
        LOG.log(Level.INFO, "{0} : destroyed", className);
    }
    
    public Optional<Boolean> control(String path){
        return Optional.empty();
    }


}
