/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    @Override
    public void init() {

    }

    @Override
    public CompletionStage<List<JWTWhiteMapEntry>> getAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void bulkRemoves(List<String> dtoRemoves) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void create(JWTWhiteMapEntry jwtWhiteMapEntry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(JWTWhiteMapEntry jwtWhiteMapEntry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
