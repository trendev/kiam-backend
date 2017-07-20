package fr.trendev.comptandye.ejbsessions;

import fr.trendev.comptandye.entities.Package;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("package")
public class PackageFacade extends AbstractFacade<Package, Long> {

    @Inject
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PackageFacade() {
        super(Package.class);
    }

}
