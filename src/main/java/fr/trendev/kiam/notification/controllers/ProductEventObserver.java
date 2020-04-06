/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.notification.controllers;

import fr.trendev.kiam.notification.entities.NotificationLevelEnum;
import fr.trendev.kiam.product.controllers.ProductEntityListener;
import fr.trendev.kiam.product.entities.EmptyThreshold;
import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.product.entities.SevereThreshold;
import fr.trendev.kiam.product.entities.WarningThreshold;
import fr.trendev.kiam.professional.controllers.ProfessionalFacade;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.thresholdalert.controllers.ThresholdAlertFacade;
import fr.trendev.kiam.thresholdalert.entities.ThresholdAlert;
import fr.trendev.kiam.thresholdalert.entities.ThresholdAlertQualifierEnum;
import fr.trendev.kiam.utils.UUIDGenerator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

/**
 *
 * @author jsie
 */
@Stateless
public class ProductEventObserver {

    @Inject
    private ThresholdAlertFacade thresholdAlertFacade;

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    private UUIDGenerator UUIDGenerator;

    private final Logger LOG = Logger.getLogger(ProductEntityListener.class.
            getName());

    public void observeWarningThresholdEvent(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) @WarningThreshold Product p) {

        try {
            this.createThresholdAlert(
                    p,
                    NotificationLevelEnum.WARNING,
                    p.getThresholdWarning(),
                    ThresholdAlertQualifierEnum.WARNING);

            LOG.log(Level.INFO,
                    "Product [{0}] for Professional Account [{1}]: availableQty has reached the WARNING threshold (qty = {2} / warning = {3})",
                    new Object[]{p.getProductReference().
                                getBarcode(), p.getProfessional().getEmail(),
                        p.getAvailableQty(), p.getThresholdWarning()});

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error emitting a WARNING notification after updating Product [{0}] for for Professional Account [{1}]",
                    new Object[]{p.getProductReference().getBarcode(), p.getProfessional().getEmail()});
        }

    }

    public void observeSevereThresholdEvent(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) @SevereThreshold Product p) {

        try {
            this.createThresholdAlert(
                    p,
                    NotificationLevelEnum.SEVERE,
                    p.getThresholdSevere(),
                    ThresholdAlertQualifierEnum.SEVERE);

            LOG.log(Level.INFO,
                    "Product [{0}] for Professional Account [{1}]: availableQty has reached the SEVERE threshold (qty = {2} / severe = {3})",
                    new Object[]{p.getProductReference().
                                getBarcode(), p.getProfessional().getEmail(),
                        p.getAvailableQty(), p.getThresholdSevere()});

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error emitting a SEVERE notification after updating Product [{0}] for for Professional Account [{1}]",
                    new Object[]{p.getProductReference().getBarcode(), p.getProfessional().getEmail()});
        }

    }

    public void observeEmptyThresholdEvent(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) @EmptyThreshold Product p) {

        try {
            this.createThresholdAlert(
                    p,
                    NotificationLevelEnum.SEVERE,
                    0,
                    ThresholdAlertQualifierEnum.EMPTY);

            String status = p.getAvailableQty() == 0 ? "is now EMPTY" : "MUST BE FILLED";

            LOG.log(Level.INFO,
                    "Product [{0}] for Professional Account [{1}]: the set {2}",
                    new Object[]{p.getProductReference().getBarcode(),
                        p.getProfessional().getEmail(),
                        status
                    });

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error emitting a SEVERE notification after updating Product [{0}] for for Professional Account [{1}]",
                    new Object[]{p.getProductReference().getBarcode(), p.getProfessional().getEmail()});
        }
    }

    private void createThresholdAlert(Product p,
            NotificationLevelEnum level,
            int threshold,
            ThresholdAlertQualifierEnum qualifier) throws Exception {

        Professional professional = professionalFacade.find(
                p.getProfessional().getEmail());

        ThresholdAlert alert = new ThresholdAlert(
                level,
                professional,
                p.getProductReference().getBarcode(),
                p.getProductReference().getDescription(),
                p.getProductReference().getBrand(),
                threshold,
                p.getAvailableQty(),
                qualifier);

        alert.setId(UUIDGenerator.generateID());

        thresholdAlertFacade.create(alert);
    }
}
