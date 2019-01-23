/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto;

import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 *
 * @author jsie
 */
public interface JWTWhiteMapDTO {

    public void init();

    public CompletionStage<List<JWTWhiteMapEntry>> getAll();

    public void bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates);

    public void bulkRemoves(List<String> dtoRemoves);

    public void create(JWTWhiteMapEntry jwtWhiteMapEntry);

    public void update(JWTWhiteMapEntry jwtWhiteMapEntry);

    public void delete(String email);
}
