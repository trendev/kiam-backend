package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.entities.ReturnedItem;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("returneditem")
public class ReturnedItemFacade extends AbstractFacade<ReturnedItem, Long> {

    @Inject
    private EntityManager em;

    public ReturnedItemFacade() {
        super(ReturnedItem.class);
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
