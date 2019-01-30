/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    private final Logger LOG;

    private URI apiUri;

    private FirestoreJWTWhiteMapProxyService proxy;

    public FirestoreJWTWhiteMapDTO() {
        this.LOG = Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName());
    }

    @Override
    public void init() {
        this.apiUri = this.loadUri();
        this.proxy = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(FirestoreJWTWhiteMapProxyService.class);
        LOG.log(Level.INFO, "{0} initialized",
                FirestoreJWTWhiteMapDTO.class.getSimpleName());
    }

    private URI loadUri() {
        try {
            // loads the properties
            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            InputStream is = classloader.getResourceAsStream(
                    "firestore/firestore.properties");

            Properties properties = new Properties();
            properties.load(is);

            String url = properties.getProperty(
                    "firestore.proxy.jwtwhitemap.url");

            LOG.
                    log(Level.INFO, "firestore.proxy.jwtwhitemap.url = \"{0}\"",
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
                FirestoreJWTWhiteMapDTO.class.getSimpleName());
    }

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {
        return this.proxy
                .getAll()
                .thenApply(list ->
                        Optional.ofNullable(list)
                                .map(l -> {
                                    LOG.log(Level.INFO,
                                            "JWTWhiteMap list got from Firestore : "
                                            + l.size() + " entries");
                                    return l;
                                })
                                .orElseGet(() -> {
                                    LOG.log(Level.WARNING,
                                            "JWTWhiteMap list got from Firestore is null !!!");
                                    return Collections.emptyList();
                                })
                )
                .exceptionally(ex -> {
                    LOG.log(Level.WARNING,
                            "Exception occurs loading JWTWhiteMap entries from "
                            + FirestoreJWTWhiteMapDTO.class.getSimpleName(), ex);
                    return Collections.emptyList();
                });
    }

    @Override
    public void bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates) {
        this.proxy.bulkUpdates(dtoUpdates)
                .thenAccept(v -> {
                    LOG.info("Bulk updates in Firestore : OK");
                });
    }

    @Override
    public void bulkRemoves(List<String> dtoRemoves) {
        this.proxy.bulkRemoves(dtoRemoves)
                .thenAccept(v -> {
                    LOG.info("Bulk removes in Firestore : OK");
                });;
    }

    @Override
    public void create(JWTWhiteMapEntry jwtWhiteMapEntry) {
        this.proxy.create(jwtWhiteMapEntry)
                .thenAccept(v -> {
                    LOG.log(Level.INFO,
                            "JWTWhiteMapEntry " + jwtWhiteMapEntry.getEmail()
                            + " has been created in Firestore");
                });
    }

    @Override
    public void update(JWTWhiteMapEntry jwtWhiteMapEntry) {
        this.proxy.update(jwtWhiteMapEntry)
                .thenAccept(v -> {
                    LOG.log(Level.INFO,
                            "JWTWhiteMapEntry " + jwtWhiteMapEntry.getEmail()
                            + " has been updated in Firestore");
                });
    }

    @Override
    public void delete(String email) {
        this.proxy.delete(email)
                .thenAccept(v -> {
                    LOG.log(Level.INFO,
                            "JWTWhiteMapEntry " + email
                            + " has been deleted in Firestore");
                });
    }

}
