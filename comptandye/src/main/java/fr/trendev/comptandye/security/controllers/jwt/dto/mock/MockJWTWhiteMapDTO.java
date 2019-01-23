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

/**
 *
 * @author jsie
 */
public class MockJWTWhiteMapDTO implements JWTWhiteMapDTO {

    @Override
    public void init() {
    }

    @Override
    public List<JWTWhiteMapEntry> getAll() {

        String token = "TK123456789";

        Instant now = Instant.now();

        Date creationDate1 = Date.from(now);
        Date expirationDate1 = Date.from(now.plus(SHORT_VALID_PERIOD,
                SHORT_VALID_PERIOD_UNIT));

        JWTRecord record1 = new JWTRecord(token, creationDate1, expirationDate1);

        Set<JWTRecord> records = new HashSet<>();
        records.add(record1);

        return Arrays.asList(new JWTWhiteMapEntry("testemail01", records));
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
