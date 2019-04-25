/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.notification.controllers;

import fr.trendev.comptandye.notification.entities.NotificationLevelEnum;
import fr.trendev.comptandye.product.entities.EmptyThreshold;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.product.controllers.ProductEntityListener;
import fr.trendev.comptandye.product.entities.SevereThreshold;
import fr.trendev.comptandye.product.entities.WarningThreshold;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.thresholdalert.controllers.ThresholdAlertFacade;
import fr.trendev.comptandye.thresholdalert.entities.ThresholdAlert;
import fr.trendev.comptandye.thresholdalert.entities.ThresholdAlertQualifierEnum;
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

    private final Logger LOG = Logger.getLogger(ProductEntityListener.class.
            getName());

    public void observeWarningThresholdEvent(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) @WarningThreshold Product p) {

        try {
            // reloads the Professional from the cache
            Professional professional = professionalFacade.find(
                    p.getProfessional().getEmail());

            // persists the notification
            thresholdAlertFacade.create(new ThresholdAlert(
                    NotificationLevelEnum.WARNING,
                    professional,
                    p.getProductReference().getBarcode(),
                    p.getProductReference().getDescription(),
                    p.getProductReference().getBrand(),
                    p.getThresholdWarning(),
                    p.getAvailableQty(),
                    ThresholdAlertQualifierEnum.WARNING
            ));

            LOG.log(Level.INFO,
                    "Product [{0}] for Professional Account [{1}]: availableQty has reached the WARNING threshold (qty = {2} / warning = {3})",
                    new Object[]{p.getProductReference().
                                getBarcode(), p.getProfessional().getEmail(),
                        p.getAvailableQty(), p.getThresholdWarning()});

        } catch (Exception ex) {
            LOG.log(Level.SEVERE,
                    "Error emitting a WARNING notification after updating Product ["
                    + p.getProductReference().getBarcode()
                    + "] for for Professional Account [" + p.
                            getProfessional().getEmail() + "]");
        }

    }

    public void observeSevereThresholdEvent(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) @SevereThreshold Product p) {

        try {
            // reloads the Professional from the cache
            Professional professional = professionalFacade.find(
                    p.getProfessional().getEmail());

            // persists the notification
            thresholdAlertFacade.create(new ThresholdAlert(
                    NotificationLevelEnum.SEVERE,
                    professional,
                    p.getProductReference().getBarcode(),
                    p.getProductReference().getDescription(),
                    p.getProductReference().getBrand(),
                    p.getThresholdSevere(),
                    p.getAvailableQty(),
                    ThresholdAlertQualifierEnum.SEVERE
            ));

            LOG.log(Level.INFO,
                    "Product [{0}] for Professional Account [{1}]: availableQty has reached the SEVERE threshold (qty = {2} / severe = {3})",
                    new Object[]{p.getProductReference().
                                getBarcode(), p.getProfessional().getEmail(),
                        p.getAvailableQty(), p.getThresholdSevere()});

        } catch (Exception ex) {
            LOG.log(Level.SEVERE,
                    "Error emitting a SEVERE notification after updating Product ["
                    + p.getProductReference().getBarcode()
                    + "] for for Professional Account [" + p.
                            getProfessional().getEmail() + "]");
        }

    }

    public void observeEmptyThresholdEvent(
            @Observes(during = TransactionPhase.AFTER_SUCCESS) @EmptyThreshold Product p) {

        try {
            // reloads the Professional from the cache
            Professional professional = professionalFacade.find(
                    p.getProfessional().getEmail());

            // persists the notification
            thresholdAlertFacade.create(new ThresholdAlert(
                    NotificationLevelEnum.SEVERE,
                    professional,
                    p.getProductReference().getBarcode(),
                    p.getProductReference().getDescription(),
                    p.getProductReference().getBrand(),
                    0,
                    p.getAvailableQty(),
                    ThresholdAlertQualifierEnum.EMPTY
            ));

            String status = p.getAvailableQty() == 0 ? "is now EMPTY" : "MUST BE FILLED";
            LOG.log(Level.INFO,
                    "Product [{0}] for Professional Account [{1}]: the set {2}",
                    new Object[]{p.getProductReference().getBarcode(),
                        p.getProfessional().getEmail(),
                        status
                    });

        } catch (Exception ex) {
            LOG.log(Level.SEVERE,
                    "Error emitting a SEVERE notification after updating Product ["
                    + p.getProductReference().getBarcode()
                    + "] for for Professional Account [" + p.
                            getProfessional().getEmail() + "]");
        }

    }
}
