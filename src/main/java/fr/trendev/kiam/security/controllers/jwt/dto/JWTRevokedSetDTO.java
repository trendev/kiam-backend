/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto;

import fr.trendev.kiam.security.entities.JWTRecord;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CompletionStage;

/**
 *
 * @author jsie
 */
public interface JWTRevokedSetDTO extends Serializable {

    public void init();

    public void close();

    public CompletionStage<Set<JWTRecord>> getAll();

    public void create(JWTRecord record);

    public void delete(String token);
}
