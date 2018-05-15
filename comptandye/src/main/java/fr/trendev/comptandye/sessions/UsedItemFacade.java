package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.UsedItem;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("useditem")
public class UsedItemFacade extends AbstractFacade<UsedItem, Long> {

    @Inject
    private EntityManager em;

    public UsedItemFacade() {
        super(UsedItem.class);
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
