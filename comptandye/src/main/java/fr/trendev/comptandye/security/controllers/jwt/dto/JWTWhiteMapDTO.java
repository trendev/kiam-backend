/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto;

import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.util.List;

/**
 *
 * @author jsie
 */
public interface JWTWhiteMapDTO {

    public void init();

    public List<JWTWhiteMapEntry> getAll();

    public void bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates);

    public void bulkRemoves(List<String> dtoRemoves);

    public void save(JWTWhiteMapEntry jwtWhiteMapEntry);

    public void remove(String email);
}
