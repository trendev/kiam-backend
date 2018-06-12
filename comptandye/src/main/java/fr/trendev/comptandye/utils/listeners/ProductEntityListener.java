/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.listeners;

import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.utils.observers.qualifiers.EmptyThreshold;
import fr.trendev.comptandye.utils.observers.qualifiers.SevereThreshold;
import fr.trendev.comptandye.utils.observers.qualifiers.WarningThreshold;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.PostUpdate;

/**
 *
 * @author jsie
 */
public class ProductEntityListener {

    @Inject
    @WarningThreshold
    private Event<Product> warningThresholdEvent;

    @Inject
    @SevereThreshold
    private Event<Product> severeThresholdEvent;

    @Inject
    @EmptyThreshold
    private Event<Product> emptyThresholdEvent;

    @PostUpdate
    void onPostUpdate(Product p) {

        if (p.getAvailableQty() <= 0) {
            emptyThresholdEvent.fire(p);
        } else {
            if (p.getAvailableQty() <= p.getThresholdSevere()) {
                severeThresholdEvent.fire(p);
            } else {
                if (p.getAvailableQty() <= p.getThresholdWarning()) {
                    warningThresholdEvent.fire(p);
                }//else available qty is ok
            }
        }

    }

}
