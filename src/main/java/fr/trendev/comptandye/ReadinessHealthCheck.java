/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.business.controllers.BusinessFacade;
import fr.trendev.comptandye.business.entities.Business;
import fr.trendev.comptandye.security.controllers.jwt.JWTManager;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
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

        try {
            tx.begin();
            Business ref = em.find(Business.class, "Informatique");

            if (ref == null) {
                throw new IllegalStateException("Reference Business cannot be null");
            }

            em.refresh(ref); // force to read the DB
            tx.commit();
            return HealthCheckResponse.builder()
                    .up()
                    .name(name)
                    .withData("business_designation", ref.getDesignation())
                    .withData("active_sessions", jwtm.getJWTWhiteMapEntries().size())
                    .build();
        } catch (Exception ex) {
            return HealthCheckResponse.builder()
                    .down()
                    .name(name)
                    .withData("message", ex.getMessage() == null ? ex.getClass().getCanonicalName() : ex.getMessage())
                    .build();
        }
    }
}
