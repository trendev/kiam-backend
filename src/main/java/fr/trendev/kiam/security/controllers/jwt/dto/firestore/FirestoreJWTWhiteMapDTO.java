/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto.firestore;

import fr.trendev.kiam.security.controllers.jwt.dto.firestore.exceptions.FirestoreProxyException;
import fr.trendev.kiam.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.kiam.security.entities.JWTWhiteMapEntry;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    private static final Logger LOG
            = Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName());

    private URI apiUri;

    private transient FirestoreJWTWhiteMapProxy proxy;

    public FirestoreJWTWhiteMapDTO() {
    }

    @Override
    public void init() {
        this.apiUri = FirestoreJWTDTOHelper.loadUri();

        LOG.log(Level.INFO, "{0} initialized",
                FirestoreJWTWhiteMapDTO.class.getSimpleName());
    }

    private FirestoreJWTWhiteMapProxy getProxy() {
        if (this.proxy == null) {
            this.proxy = FirestoreJWTDTOHelper.buildProxy(apiUri,
                    FirestoreJWTWhiteMapProxy.class);
        }
        return this.proxy;
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "{0} closed",
                FirestoreJWTWhiteMapDTO.class.getSimpleName());
    }

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {

        final String errMsg
                = "Exception occurs getting all JWTWhiteMap entries from Firestore";

        try {
            return this.getProxy()
                    .getAll()
                    .thenApply(list
                            -> Optional.ofNullable(list)
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
                    .exceptionally(ex -> FirestoreJWTDTOHelper.errorHandler(
                    ex,
                    errMsg,
                    Collections.emptyList()));
        } catch (FirestoreProxyException ex) {
            return CompletableFuture.completedFuture(
                    FirestoreJWTDTOHelper.errorHandler(
                            ex,
                            errMsg,
                            Collections.emptyList()));
        }

    }

    @Override
    public void create(JWTWhiteMapEntry jwtWhiteMapEntry) {
        FirestoreJWTDTOHelper.manageSilentOperations(this.getProxy(),
                "JWTWhiteMapEntry "
                + jwtWhiteMapEntry.getEmail()
                + " has been created in Firestore",
                "Exception occurs creating a JWTWhiteMapEntry in Firestore",
                FirestoreJWTWhiteMapProxy::create,
                jwtWhiteMapEntry);
    }

    @Override
    public void update(JWTWhiteMapEntry jwtWhiteMapEntry) {
        FirestoreJWTDTOHelper.manageSilentOperations(this.getProxy(),
                "JWTWhiteMapEntry "
                + jwtWhiteMapEntry.getEmail()
                + " has been updated in Firestore",
                "Exception occurs updating a JWTWhiteMapEntry in Firestore",
                FirestoreJWTWhiteMapProxy::update,
                jwtWhiteMapEntry);
    }

    @Override
    public void delete(String email) {
        FirestoreJWTDTOHelper.manageSilentOperations(this.getProxy(),
                "JWTWhiteMapEntry "
                + email
                + " has been deleted in Firestore",
                "Exception occurs deleting a JWTWhiteMapEntry in Firestore",
                FirestoreJWTWhiteMapProxy::delete,
                email);
    }

}
