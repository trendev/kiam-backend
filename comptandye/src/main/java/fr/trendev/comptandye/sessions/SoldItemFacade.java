package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.entities.SoldItem;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("solditem")
public class SoldItemFacade extends AbstractFacade<SoldItem, Long> {

    @Inject
    private EntityManager em;

    public SoldItemFacade() {
        super(SoldItem.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public String prettyPrintPK(Long pk) {
        return pk.toString();
    }

}
