/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.mock;

import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class MockErrorsJWTWhiteMapDTO extends MockJWTWhiteMapDTO {

    private static final Logger LOG = Logger.getLogger(
            MockErrorsJWTWhiteMapDTO.class.getName());

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {
        return super.getAll()
                .thenApply(list -> {
                    if (!list.isEmpty()) {
                        throw new RuntimeException("EXCEPTION_DTO");
                    }
                    return list;
                })
                .exceptionally(ex -> {
                    LOG.log(Level.SEVERE,
                            "Exception occurs loading JWTWhiteMap entries from "
                            + MockJWTWhiteMapDTO.class.getSimpleName(), ex);
                    return Collections.emptyList();
                });
    }

}
