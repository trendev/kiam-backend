/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.PurchasedOffering;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public abstract class AbstractOfferingService<T extends Offering> extends AbstractCommonService<T, OfferingPK> {

    private final Logger LOG = Logger.getLogger(
            AbstractBillService.class.
                    getName());

    public AbstractOfferingService(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    public Response getPurchasedOfferings(OfferingPK pk) {
        return super.provideRelation(pk, Offering::getPurchasedOfferings,
                PurchasedOffering.class);
    }

    public Response delete(OfferingPK pk) {
        return super.delete(pk,
                e -> {
            //break the link between the offering and the bill's purchasedoffering (if necessary)
            e.getPurchasedOfferings().forEach(po -> po.setOffering(null));
            e.setPurchasedOfferings(null);

            //remove the offering from the professional's offering list
            e.getProfessional().getOfferings().remove(e);
        });
    }

}
