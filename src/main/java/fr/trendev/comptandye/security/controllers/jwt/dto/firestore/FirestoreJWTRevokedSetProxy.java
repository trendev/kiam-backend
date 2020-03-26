/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.entities.JWTRecord;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
public interface FirestoreJWTRevokedSetProxy extends Serializable {

    @GET
    CompletionStage<Set<JWTRecord>> getAll() throws FirestoreProxyException;

    @POST
    CompletionStage<JWTRecord> create(JWTRecord record) throws
            FirestoreProxyException;

    @POST
    @Path("bulk-creation")
    CompletionStage<Set<JWTRecord>> bulkCreation(Set<JWTRecord> records) throws
            FirestoreProxyException;

    @DELETE
    @Path("{token}")
    CompletionStage<String> delete(@PathParam("token") String token) throws
            FirestoreProxyException;

}
