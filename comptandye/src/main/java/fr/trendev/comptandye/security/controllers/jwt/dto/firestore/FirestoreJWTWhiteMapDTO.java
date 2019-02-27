/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    private static final Logger LOG =
            Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName());

    private URI apiUri;

    private transient FirestoreJWTWhiteMapProxyService proxy;

    public FirestoreJWTWhiteMapDTO() {
    }

    @Override
    public void init() {
        this.apiUri = FirestoreJWTDTOHelper.loadUri(LOG,
                "firestore.proxy.jwtwhitemap.url");

        LOG.log(Level.INFO, "{0} initialized",
                FirestoreJWTWhiteMapDTO.class.getSimpleName());
    }

    private FirestoreJWTWhiteMapProxyService getProxy() {
        if (this.proxy == null) {
            this.proxy = RestClientBuilder.newBuilder()
                    .baseUri(apiUri)
                    .register(FirestoreProxyExceptionMapper.class)
                    .build(FirestoreJWTWhiteMapProxyService.class);
            LOG.log(Level.INFO, "New instance of "
                    + FirestoreJWTWhiteMapProxyService.class.getSimpleName()
                    + " created");
        }
        return this.proxy;
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "{0} closed",
                FirestoreJWTWhiteMapDTO.class.getSimpleName());
    }

    private <T> T errorHandler(Throwable ex,
            String message,
            T t) {
        LOG.log(Level.INFO, message, ex);
        LOG.log(Level.WARNING, message);
        return t;
    }

    private final <T> void manageSilentOperations(
            String successMsg,
            String errMsg,
            ThrowingBiFunction<FirestoreJWTWhiteMapProxyService, T, CompletionStage<Void>> fn,
            T t) {
        try {
            fn.applyThrows(this.getProxy(), t)
                    .thenRun(() -> LOG.info(successMsg))
                    .exceptionally(ex -> this.errorHandler(ex, errMsg, null));
        } catch (Throwable ex) {
            this.errorHandler(ex, errMsg, null);
        }
    }

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {

        final String errMsg =
                "Exception occurs getting all JWTWhiteMap entries from Firestore";

        try {
            return this.getProxy()
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
                    .exceptionally(ex -> this.errorHandler(
                            ex,
                            errMsg,
                            Collections.emptyList()));
        } catch (FirestoreProxyException ex) {
            return CompletableFuture.completedFuture(this.errorHandler(
                    ex,
                    errMsg,
                    Collections.emptyList()));
        }

    }

    @Override
    public void bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates) {
        manageSilentOperations("Bulk JWTWhiteMap updates in Firestore : OK",
                "Exception occurs putting multiple JWTWhiteMap updates to Firestore",
                FirestoreJWTWhiteMapProxyService::bulkUpdates, dtoUpdates);

    }

    @Override
    public void bulkRemoves(List<String> dtoRemoves) {
        manageSilentOperations("Bulk JWTWhiteMap removes in Firestore : OK",
                "Exception occurs deleting multiple JWTWhiteMap entries to Firestore",
                FirestoreJWTWhiteMapProxyService::bulkRemoves, dtoRemoves);
    }

    @Override
    public void create(JWTWhiteMapEntry jwtWhiteMapEntry) {
        manageSilentOperations("JWTWhiteMapEntry "
                + jwtWhiteMapEntry.getEmail()
                + " has been created in Firestore",
                "Exception occurs creating a JWTWhiteMapEntry in Firestore",
                FirestoreJWTWhiteMapProxyService::create, jwtWhiteMapEntry);
    }

    @Override
    public void update(JWTWhiteMapEntry jwtWhiteMapEntry) {
        manageSilentOperations("JWTWhiteMapEntry "
                + jwtWhiteMapEntry.getEmail()
                + " has been updated in Firestore",
                "Exception occurs updating a JWTWhiteMapEntry in Firestore",
                FirestoreJWTWhiteMapProxyService::update, jwtWhiteMapEntry);
    }

    @Override
    public void delete(String email) {
        manageSilentOperations("JWTWhiteMapEntry "
                + email
                + " has been deleted in Firestore",
                "Exception occurs deleting a JWTWhiteMapEntry in Firestore",
                FirestoreJWTWhiteMapProxyService::delete, email);
    }

}
