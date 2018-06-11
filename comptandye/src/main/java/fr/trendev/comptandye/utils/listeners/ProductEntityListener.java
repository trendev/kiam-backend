/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.listeners;

import fr.trendev.comptandye.entities.Product;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.PostUpdate;

/**
 *
 * @author jsie
 */
public class ProductEntityListener {

    @Inject
    private Event<Product> warningThresholdEvent;

    @PostUpdate
    void onPostUpdate(Product p) {
        if (p.getAvailableQty() <= p.getThresholdWarning()) {
            warningThresholdEvent.fire(p);
        }
    }
}
