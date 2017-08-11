package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Individual;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("individual")
public class IndividualFacade extends AbstractFacade<Individual, String> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public IndividualFacade() {
        super(Individual.class);
    }

}
