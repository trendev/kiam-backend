/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.mock;

import static fr.trendev.comptandye.security.controllers.jwt.JWTManager.SHORT_VALID_PERIOD;
import static fr.trendev.comptandye.security.controllers.jwt.JWTManager.SHORT_VALID_PERIOD_UNIT;
import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTRecord;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
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

/**
 *
 * @author jsie
 */
public class MockJWTWhiteMapDTO implements JWTWhiteMapDTO {

    public static long LATENCY = 200l;
    private static final Logger LOG =
            Logger.getLogger(MockJWTWhiteMapDTO.class.getName());

    @Override
    public void init() {
        LOG.info(MockJWTWhiteMapDTO.class.getSimpleName() + " initialized");
    }

    @Override
    public void close() {
        LOG.info(MockJWTWhiteMapDTO.class.getSimpleName() + " closed");
    }

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {

        return CompletableFuture.supplyAsync(() -> {
            String token = "TK123456789";

            Instant now = Instant.now();

            Date creationDate1 = Date.from(now);
            Date expirationDate1 = Date.from(now.plus(SHORT_VALID_PERIOD,
                    SHORT_VALID_PERIOD_UNIT));

            JWTRecord record1 = new JWTRecord(token, creationDate1,
                    expirationDate1);

            Set<JWTRecord> records = new HashSet<>();
            records.add(record1);

            List<JWTWhiteMapEntry> list =
                    Arrays.asList(new JWTWhiteMapEntry("testemail01", records));

            //simulate latency
            LOG.log(Level.INFO,
                    "getAll() in {0} : Mock list is ready but wait (simulate latency) {1} ms / Thread = {2}",
                    new Object[]{
                        MockJWTWhiteMapDTO.class.getSimpleName(),
                        LATENCY,
                        Thread.currentThread().getName()});
            try {
                Thread.sleep(LATENCY);
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE,
                        MockJWTWhiteMapDTO.class.getSimpleName()
                        + " must not be interrupted",
                        ex);
            }

            return list;
        });
    }

    @Override
    public void bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates) {
    }

    @Override
    public void bulkRemoves(List<String> dtoRemoves) {
    }

    @Override
    public void create(JWTWhiteMapEntry jwtWhiteMapEntry) {
    }

    @Override
    public void update(JWTWhiteMapEntry jwtWhiteMapEntry) {
    }

    @Override
    public void delete(String email) {
    }
}
