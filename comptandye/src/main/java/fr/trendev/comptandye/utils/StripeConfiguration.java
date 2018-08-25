/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

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

    @PostConstruct
    void init() {
        Stripe.apiKey = "sk_test_0gkdtCawLunLNk2sPRCSrTDv";
    }

}
