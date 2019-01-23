/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.mock;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.util.Collections;
import java.util.List;

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
        return Collections.emptyList();
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
