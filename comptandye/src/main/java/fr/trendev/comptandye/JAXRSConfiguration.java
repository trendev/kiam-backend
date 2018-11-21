/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye;

import java.util.Map;
import java.util.TreeMap;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author jsie
 */
@ApplicationScoped
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

    @Inject
    private Pbkdf2PasswordHash pbkdf2Hash;

    @Override
    public Map<String, Object> getProperties() {
        Map<String, Object> map = new TreeMap<>();
        map.put("jersey.config.jsonFeature", "JacksonFeature");
        return map;
    }
}
