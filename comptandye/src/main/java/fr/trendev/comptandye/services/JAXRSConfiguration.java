/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import java.util.Map;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author jsie
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> map = super.getProperties();
//        map.put("jersey.config.jsonFeature", JacksonFeature.class);
        return map;
    }

//    public Set<Class<?>> getClasses() {
//        final Set<Class<?>> classes = new HashSet<Class<?>>();
//
//        // Add JacksonFeature.
//        classes.add(JacksonFeature.class);
//
//        return classes;
//    }
}
