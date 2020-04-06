/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.exceptions.FirestoreProxyException;
import fr.trendev.comptandye.security.controllers.jwt.dto.JWTRevokedSetDTO;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTRevokedSetDTO implements JWTRevokedSetDTO {

    private static final Logger LOG
            = Logger.getLogger(FirestoreJWTRevokedSetDTO.class.getName());

    private URI apiUri;

    private transient FirestoreJWTRevokedSetProxy proxy;

    public FirestoreJWTRevokedSetDTO() {
    }

    @Override
    public void init() {
        this.apiUri = FirestoreJWTDTOHelper.loadUri();

        LOG.log(Level.INFO, "{0} initialized",
                FirestoreJWTRevokedSetDTO.class.getSimpleName());
    }

    private FirestoreJWTRevokedSetProxy getProxy() {
        if (this.proxy == null) {
            this.proxy = FirestoreJWTDTOHelper.buildProxy(apiUri,
                    FirestoreJWTRevokedSetProxy.class);
        }
        return this.proxy;
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "{0} closed",
                FirestoreJWTRevokedSetDTO.class.getSimpleName());
    }

    @Override
    public CompletionStage<Set<JWTRecord>> getAll() {
        final String errMsg
                = "Exception occurs getting all Revoked JWT entries from Firestore";

        try {
            return this.getProxy()
                    .getAll()
                    .thenApply(set
                            -> Optional.ofNullable(set)
                            .map(s -> {
                                LOG.log(Level.INFO,
                                        "Revoked JWT Set got from Firestore : "
                                        + s.size() + " entries");
                                return s;
                            })
                            .orElseGet(() -> {
                                LOG.log(Level.WARNING,
                                        "Revoked JWT Set got from Firestore is null !!!");
                                return Collections.emptySet();
                            })
                    )
                    .exceptionally(ex -> FirestoreJWTDTOHelper.errorHandler(
                    ex,
                    errMsg,
                    Collections.emptySet()));
        } catch (FirestoreProxyException ex) {
            return CompletableFuture.completedFuture(
                    FirestoreJWTDTOHelper.errorHandler(
                            ex,
                            errMsg,
                            Collections.emptySet()));
        }
    }

    @Override
    public void create(JWTRecord record) {
        FirestoreJWTDTOHelper.manageSilentOperations(this.getProxy(),
                "Revoked JWT "
                + record.getToken()
                + " has been created in Firestore",
                "Exception occurs creating a Revoked JWT entry in Firestore",
                FirestoreJWTRevokedSetProxy::create,
                record);
    }

    @Override
    public void delete(String token) {
        FirestoreJWTDTOHelper.manageSilentOperations(this.getProxy(),
                "Revoked JWT "
                + token
                + " has been deleted in Firestore",
                "Exception occurs deleting a Revoked JWT entry in Firestore",
                FirestoreJWTRevokedSetProxy::delete,
                token);
    }

}
