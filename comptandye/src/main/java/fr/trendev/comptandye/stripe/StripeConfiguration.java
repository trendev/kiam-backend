/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.stripe;

import com.stripe.Stripe;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private final Logger LOG = Logger.getLogger(StripeConfiguration.class.
            getName());

    /**
     * Loads and Initializes the Stripe API KEY with the TEST or LIVE Stripe Key
     * (depending of the PROD or DEV environment)
     */
    @PostConstruct
    void init() {
        try {
            // loads the properties
            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(
                    "stripe/stripe.properties");

            Properties properties = new Properties();
            properties.load(is);

            String key = properties.getProperty("stripe.key");
            String type = properties.getProperty("stripe.type");

            // sets the stripe api key
            Stripe.apiKey = key;

            LOG.log(Level.INFO, "Stripe {0} key is set ({1})",
                    new Object[]{type,
                        key});

        } catch (IOException ex) { //should not happen
            LOG.log(Level.SEVERE, "STRIPE API KEY IS NOT SET", ex);
        }
    }

}
