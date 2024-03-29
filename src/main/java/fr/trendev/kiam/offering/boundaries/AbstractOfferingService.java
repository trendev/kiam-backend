/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.offering.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.offering.entities.Offering;
import fr.trendev.kiam.offering.entities.OfferingPK;
import fr.trendev.kiam.pack.controllers.PackFacade;
import fr.trendev.kiam.pack.entities.Pack;
import fr.trendev.kiam.professional.controllers.ProfessionalFacade;
import fr.trendev.kiam.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.kiam.sale.controllers.SaleFacade;
import fr.trendev.kiam.service.controllers.ServiceFacade;
import fr.trendev.kiam.offering.controllers.OfferingIntegrityVisitor;
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
    protected PackFacade packFacade;

    @Inject
    protected ProfessionalFacade professionalFacade;

    @Inject
    protected ServiceFacade serviceFacade;

    @Inject
    protected SaleFacade saleFacade;

    private final Logger LOG = Logger.getLogger(
            AbstractOfferingService.class.
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
