/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto;

import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
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

    public void bulkRemoves(List<String> dtoRemoves);

    public void create(JWTRecord record);

    public void bulkCreation(Collection<JWTRecord> records);

    public void delete(String token);
}
