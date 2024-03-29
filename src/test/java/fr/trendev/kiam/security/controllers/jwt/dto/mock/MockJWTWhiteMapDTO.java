/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto.mock;

import fr.trendev.kiam.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.kiam.security.entities.JWTRecord;
import fr.trendev.kiam.security.entities.JWTWhiteMapEntry;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY;
import static fr.trendev.kiam.security.controllers.jwt.JWTManager.SHORT_TERM_VALIDITY_UNIT;

/**
 *
 * @author jsie
 */
public class MockJWTWhiteMapDTO implements JWTWhiteMapDTO {

    private static final Logger LOG =
            Logger.getLogger(MockJWTWhiteMapDTO.class.getName());

    @Override
    public void init() {
        LOG.log(Level.INFO, "MOCK - {0} initialized", MockJWTWhiteMapDTO.class.
                getSimpleName());
    }

    @Override
    public void close() {
        LOG.log(Level.INFO, "MOCK - {0} closed", MockJWTWhiteMapDTO.class.
                getSimpleName());
    }

    private List<JWTWhiteMapEntry> getMockList() {
        String token = "TK123456789";

        Instant now = Instant.now();

        Date creationDate1 = Date.from(now);
        Date expirationDate1 = Date.from(now.plus(SHORT_TERM_VALIDITY,
                SHORT_TERM_VALIDITY_UNIT));

        JWTRecord record1 = new JWTRecord(token, creationDate1,
                expirationDate1);

        Set<JWTRecord> records = new HashSet<>();
        records.add(record1);

        return Arrays.asList(new JWTWhiteMapEntry("testemail01", records));
    }

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {
        LOG.info("MOCK - Getting all JWTWhiteMap entries from Firestore");
        return CompletableFuture.completedFuture(this.getMockList());
    }

    @Override
    public void create(JWTWhiteMapEntry jwtWhiteMapEntry) {
        LOG.log(Level.INFO,
                "MOCK - Creating a JWTWhiteMap entry for user {0} in Firestore",
                jwtWhiteMapEntry.getEmail());
    }

    @Override
    public void update(JWTWhiteMapEntry jwtWhiteMapEntry) {
        LOG.log(Level.INFO,
                "MOCK - Updating a JWTWhiteMap entry for user {0} in Firestore",
                jwtWhiteMapEntry.getEmail());
    }

    @Override
    public void delete(String email) {
        LOG.log(Level.INFO,
                "MOCK - Removing all JWTWhiteMap entrie of user {0} from Firestore",
                email);
    }
}
