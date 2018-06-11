/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.observers;

import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.utils.listeners.ProductEntityListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;

/**
 *
 * @author jsie
 */
@Stateless
public class ProductEventObserver {

    private final Logger LOG = Logger.getLogger(ProductEntityListener.class.
            getName());

    public void observeWarningThresholdEvent(@Observes Product p) {
        LOG.log(Level.INFO,
                "Product [{0}] for Professional Account [{1}]: availableQty has reached the WARNING threshold (qty = {2} / warning = {3})",
                new Object[]{p.getProductReference().
                            getBarcode(), p.getProfessional().getEmail(),
                    p.getAvailableQty(), p.getThresholdWarning()});
    }
}
