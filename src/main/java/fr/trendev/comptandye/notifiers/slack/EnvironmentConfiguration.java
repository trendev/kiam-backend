/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.notifiers.slack;

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
public class EnvironmentConfiguration {

    private static final Logger LOG = Logger.getLogger(EnvironmentConfiguration.class.getName());
    
    private String type;
    
    @PostConstruct
    void init() {
        ClassLoader classloader = Thread.currentThread().
                getContextClassLoader();

        try (InputStream is = classloader.getResourceAsStream(
                "env/environment.properties")) {

            Properties properties = new Properties();
            properties.load(is);

            // loads the properties
            this.type = properties.getProperty("environment.type");

            LOG.log(Level.INFO, "--- Environment : {0} ---",
                    new Object[]{this.type});

        } catch (IOException ex) { //should not happen
            LOG.log(Level.SEVERE, "Environment is not loaded has expected...", ex);
        }
    }
    
    public String getType(){
        return this.type;
    }
    
}
