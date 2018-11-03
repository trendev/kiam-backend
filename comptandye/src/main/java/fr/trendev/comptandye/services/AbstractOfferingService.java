/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.sessions.PackFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.SaleFacade;
import fr.trendev.comptandye.sessions.ServiceFacade;
import fr.trendev.comptandye.utils.visitors.OfferingIntegrityVisitor;
import java.util.function.Consumer;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public abstract class AbstractOfferingService<T extends Offering> extends AbstractCommonService<T, OfferingPK> {

    @Inject
    PackFacade packFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ServiceFacade serviceFacade;

    @Inject
    SaleFacade saleFacade;

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

    protected void getPurchasedOfferings(final AsyncResponse ar,
            OfferingPK pk) {
        super.provideRelation(ar, pk, Offering::getPurchasedOfferings,
                PurchasedOffering.class);
    }

    protected void getParentPacks(final AsyncResponse ar,
            OfferingPK pk) {
        super.provideRelation(ar, pk, Offering::getParentPacks,
                Pack.class);
    }

    protected Response delete(OfferingPK pk,
            Consumer<T> removeChildrenDependencies) {
        return super.delete(pk,
                e -> {
            //break the link between the offering and the bill's purchasedoffering (if necessary)
            e.getPurchasedOfferings().forEach(po -> po.setOffering(null));
            e.setPurchasedOfferings(null);

            //remove itself from the parentPacks
            e.getParentPacks().forEach(pp -> pp.getOfferings().remove(e));
            e.setParentPacks(null);

            //remove a pack from its children parentPacks dependencies
            //useless for services
            removeChildrenDependencies.accept(e);

            //remove the offering from the professional's offering list
            e.getProfessional().getOfferings().remove(e);
        });
    }

    protected Offering checkOfferingIntegrity(Offering o, String proEmail) {
        OfferingIntegrityVisitor v = new OfferingIntegrityVisitor(packFacade,
                serviceFacade, saleFacade, proEmail);
        return o.accept(v);
    }

}
