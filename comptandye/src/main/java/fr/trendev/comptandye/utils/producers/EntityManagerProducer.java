package fr.trendev.comptandye.utils.producers;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Producer for injectable EntityManager
 *
 */
@ApplicationScoped
public class EntityManagerProducer {

    @Produces
    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;
}
