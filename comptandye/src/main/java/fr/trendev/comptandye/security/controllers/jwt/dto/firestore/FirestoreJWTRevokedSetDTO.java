/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTRevokedSetDTO;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTRevokedSetDTO implements JWTRevokedSetDTO {

    private static final Logger LOG =
            Logger.getLogger(FirestoreJWTRevokedSetDTO.class.getName());

    private URI apiUri;

    public FirestoreJWTRevokedSetDTO() {
    }

    @Override
    public void init() {
        this.apiUri = this.loadUri();

        LOG.log(Level.INFO, "{0} initialized",
                FirestoreJWTRevokedSetDTO.class.getSimpleName());
    }

    private URI loadUri() {

        ClassLoader classloader = Thread.currentThread().
                getContextClassLoader();

        try (InputStream is = classloader.getResourceAsStream(
                "firestore/firestore.properties")) {

            Properties properties = new Properties();
            properties.load(is);

            // loads the properties
            String url = properties.getProperty(
                    "firestore.proxy.jwtrevokedset.url");

            LOG.log(Level.INFO,
                    "firestore.proxy.jwtrevokedset.url = \"{0}\"",
                    url);

            return new URI(url);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(
                    "Url provided in properties is not valid", ex);
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "IO Errors setting Firestore properties", ex);
        }
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "{0} closed",
                FirestoreJWTRevokedSetDTO.class.getSimpleName());
    }

    @Override
    public CompletionStage<Set<JWTRecord>> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bulkRemoves(List<String> dtoRemoves) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(JWTRecord record) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bulkCreation(Collection<JWTRecord> records) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String token) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
