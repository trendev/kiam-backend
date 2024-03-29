/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.stripe;

import com.stripe.Stripe;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author jsie
 */
@Startup
@Singleton
public class StripeConfiguration {

    private final Logger LOG = Logger.getLogger(StripeConfiguration.class.
            getName());

    @Inject
    @ConfigProperty(name = "STRIPE_DEFAULT_TAX_RATE_ID")
    private String defaultTaxRateID;

    @Inject
    @ConfigProperty(name = "STRIPE_KEY")
    private String key;

    @Inject
    @ConfigProperty(name = "STRIPE_TYPE")
    private String type;

    /**
     * Loads and Initializes the Stripe API KEY with the TEST or LIVE Stripe Key
     * (depending of the PROD or DEV environment)
     */
    @PostConstruct
    void init() {

        LOG.log(Level.INFO, "STRIPE_DEFAULT_TAX_RATE_ID : {0}\nSTRIPE_KEY : {1}\nSTRIPE_TYPE : {2}",
                new Object[]{this.getDefaultTaxRateID(), this.getKey(), this.getType()});

        // sets the stripe api key
        Stripe.apiKey = this.getKey();

        LOG.log(Level.INFO, "Stripe {0} key is set : {1}",
                new Object[]{this.getType(),
                    this.getKey()});

    }

    public String getDefaultTaxRateID() {
        if (this.defaultTaxRateID == null
                || this.defaultTaxRateID.isEmpty()) {
            throw new IllegalStateException("Stripe's DefaultTaxRateID cannot be null or empty");
        }
        return this.defaultTaxRateID;
    }

    private String getKey() {
        if (this.key == null
                || this.key.isEmpty()) {
            throw new IllegalStateException("Stripe's Key cannot be null or empty");
        }
        return this.key;
    }

    private String getType() {
        if (this.type == null
                || this.type.isEmpty()) {
            throw new IllegalStateException("Stripe's Type cannot be null or empty (should be TEST or LIVE)");
        }
        return this.type;
    }

}
