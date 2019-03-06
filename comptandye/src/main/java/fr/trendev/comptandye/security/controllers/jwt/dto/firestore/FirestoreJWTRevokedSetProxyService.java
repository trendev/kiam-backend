/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author jsie
 */
@Path("jwtrevokedset")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface FirestoreJWTRevokedSetProxyService extends Serializable {

    @GET
    CompletionStage<Set<JWTRecord>> getAll() throws FirestoreProxyException;

    @DELETE
    @Path("removes")
    CompletionStage<Void> bulkRemoves(List<String> dtoRemoves) throws
            FirestoreProxyException;

}
