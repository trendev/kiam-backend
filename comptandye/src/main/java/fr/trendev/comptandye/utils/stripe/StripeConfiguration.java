/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.stripe;

import com.stripe.Stripe;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class StripeConfiguration {

    /**
     * Initializes the Stripe API KEY with the TEST or LIVE Stripe Key
     */
    @PostConstruct
    void init() {
        Stripe.apiKey = StripeApiKey.TEST.getKey();
    }

    private enum StripeApiKey {
        TEST("sk_test_0gkdtCawLunLNk2sPRCSrTDv"),
        LIVE("sk_live_wSpz6KIOBRcHoMUMhtUdB0vU");

        private StripeApiKey(String key) {
            this.key = key;
        }

        private final String key;

        String getKey() {
            return this.key;
        }
    }

}
