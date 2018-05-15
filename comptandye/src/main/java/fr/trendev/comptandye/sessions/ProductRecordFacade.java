package fr.trendev.comptandye.sessions;

import fr.trendev.comptandye.entities.ProductRecord;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

@Stateless
@Named("productrecord")
public class ProductRecordFacade extends AbstractFacade<ProductRecord, Long> {

    @Inject
    private EntityManager em;

    public ProductRecordFacade() {
        super(ProductRecord.class);
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
