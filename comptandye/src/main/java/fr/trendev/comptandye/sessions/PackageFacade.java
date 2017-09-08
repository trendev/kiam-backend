package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.Pack;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("package")
public class PackageFacade extends AbstractFacade<Pack, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PackageFacade() {
        super(Pack.class);
    }

}
