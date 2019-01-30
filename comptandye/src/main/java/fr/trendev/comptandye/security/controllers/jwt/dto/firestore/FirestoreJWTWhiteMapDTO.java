/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
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
        this.loadUri();
        this.proxy = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .build(FirestoreJWTWhiteMapProxyService.class);
    }

    private void loadUri() {
        //TODO : load from env properties
        String uri = "http://localhost:9080/firestore-proxy";

        try {
            this.apiUri = new URI(uri);
        } catch (URISyntaxException ex) {
            LOG.log(Level.SEVERE, null, ex);
            throw new IllegalStateException(uri + "URI is not valid");

        }
    }

    @Override
    public void close() {
    }

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {
        return this.proxy
                .getAll()
                .exceptionally(ex -> {
                    LOG.log(Level.SEVERE,
                            "Exception occurs loading JWTWhiteMap entries from "
                            + FirestoreJWTWhiteMapDTO.class.getSimpleName(), ex);
                    return Collections.emptyList();
                });
    }

    @Override
    public void bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates) {
        this.proxy.bulkUpdates(dtoUpdates);
    }

    @Override
    public void bulkRemoves(List<String> dtoRemoves) {
        this.proxy.bulkRemoves(dtoRemoves);
    }

    @Override
    public void create(JWTWhiteMapEntry jwtWhiteMapEntry) {
        this.proxy.create(jwtWhiteMapEntry);
    }

    @Override
    public void update(JWTWhiteMapEntry jwtWhiteMapEntry) {
        this.update(jwtWhiteMapEntry);
    }

    @Override
    public void delete(String email) {
        this.delete(email);
    }

}
