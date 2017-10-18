package fr.trendev.comptandye.utils.producers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
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

    private static final Logger LOG = Logger.getLogger(
            EntityManagerProducer.class.getName());

    @Produces
    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    @PreDestroy
    public void onDestroy() {
        LOG.log(Level.INFO, "EntityManagerProducer is stopping");
    }
}
