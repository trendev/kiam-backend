package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Professional;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("professional")
public class ProfessionalFacade extends AbstractFacade<Professional, String> {

    @Inject
    private EntityManager em;

    public ProfessionalFacade() {
        super(Professional.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(String pk) {
        return pk;
    }

}
