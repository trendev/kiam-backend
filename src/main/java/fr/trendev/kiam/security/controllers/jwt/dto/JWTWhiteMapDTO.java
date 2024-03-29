/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto;

import fr.trendev.kiam.security.entities.JWTWhiteMapEntry;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletionStage;

/**
 *
 * @author jsie
 */
public interface JWTWhiteMapDTO extends Serializable {

    public void init();

    public void close();

    public CompletionStage<List<JWTWhiteMapEntry>> getAll();

    public void create(JWTWhiteMapEntry jwtWhiteMapEntry);

    public void update(JWTWhiteMapEntry jwtWhiteMapEntry);

    public void delete(String email);
}
