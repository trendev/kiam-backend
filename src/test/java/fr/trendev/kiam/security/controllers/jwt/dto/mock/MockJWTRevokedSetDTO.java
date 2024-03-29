/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto.mock;

import fr.trendev.kiam.security.controllers.jwt.dto.JWTRevokedSetDTO;
import fr.trendev.kiam.security.entities.JWTRecord;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class MockJWTRevokedSetDTO implements JWTRevokedSetDTO {

    private static final Logger LOG =
            Logger.getLogger(MockJWTRevokedSetDTO.class.getName());

    @Override
    public void init() {
        LOG.log(Level.INFO, "MOCK - {0} initialized",
                JWTRevokedSetDTO.class.getSimpleName());
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "MOCK - {0} closed",
                JWTRevokedSetDTO.class.getSimpleName());
    }

    @Override
    public CompletionStage<Set<JWTRecord>> getAll() {
        LOG.info("MOCK - Getting all revoked token from Firestore");
        return CompletableFuture.completedFuture(Collections.emptySet());
    }

    @Override
    public void create(JWTRecord record) {
        LOG.log(Level.INFO, "MOCK - Adding revoked token {0} in Firestore",
                record.getToken());
    }

    @Override
    public void delete(String token) {
        LOG.log(Level.INFO, "MOCK - Removing revoked token {0} from Firestore",
                token);
    }

}
