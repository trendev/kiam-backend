package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.PurchasedItem;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("purchaseditem")
public class PurchasedItemFacade extends AbstractFacade<PurchasedItem, Long> {

    @Inject
    private EntityManager em;

    public PurchasedItemFacade() {
        super(PurchasedItem.class);
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
