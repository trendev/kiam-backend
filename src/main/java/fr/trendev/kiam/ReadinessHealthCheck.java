/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye;

import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.security.controllers.jwt.JWTManager;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

/**
 *
 * @author jsie
 */
@Readiness
@ApplicationScoped
public class ReadinessHealthCheck implements HealthCheck {

    @Inject
    private EntityManager em;

    @Inject
    private UserTransaction tx;

    @Inject
    private JWTManager jwtm;

    private final String name = "readyness health check";

    @Override
    public HealthCheckResponse call() {

        HealthCheckResponseBuilder builder = HealthCheckResponse.builder().name(name);

        // control if DB can be read
        try {
            tx.begin();
            Business ref = em.find(Business.class, "Informatique");

            if (ref == null) {
                throw new IllegalStateException("Reference Business cannot be null");
            }

            em.refresh(ref); // bypass the cache and force to DB reads
            tx.commit();
            builder.up()
                    .withData("DB_status", "UP");
        } catch (Exception ex) {
            builder.down()
                    .withData("DB_status", "DOWN")
                    .withData("db_test_message", ex.getMessage() == null ? ex.getClass().getCanonicalName() : ex.getMessage());
        }
        
        // control main security structure can be read
        try{
            int count = jwtm.getJWTWhiteMapEntries().size();
            builder.withData("active_sessions", count);
        }catch(Exception ex){
            builder.down()
                    .withData("jwt_whitemap_test_message", ex.getMessage() == null ? ex.getClass().getCanonicalName() : ex.getMessage());
        }
        
        return builder.build();
    }
}
