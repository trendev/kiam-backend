/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletionStage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author jsie
 */
@Path("jwtwhitemap")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface FirestoreJWTWhiteMapProxyService extends Serializable {

    @GET
    CompletionStage<List<JWTWhiteMapEntry>> getAll() throws
            FirestoreProxyException;

    @PUT
    @Path("bulk-updates")
    CompletionStage<List<JWTWhiteMapEntry>> bulkUpdates(
            List<JWTWhiteMapEntry> dtoUpdates)
            throws
            FirestoreProxyException;

    @PUT
    @Path("bulk-removes")
    CompletionStage<List<String>> bulkRemoves(List<String> dtoRemoves) throws
            FirestoreProxyException;

    @POST
    CompletionStage<JWTWhiteMapEntry> create(JWTWhiteMapEntry jwtWhiteMapEntry)
            throws
            FirestoreProxyException;

    @PUT
    CompletionStage<JWTWhiteMapEntry> update(JWTWhiteMapEntry jwtWhiteMapEntry)
            throws
            FirestoreProxyException;

    @DELETE
    @Path("{email}")
    CompletionStage<String> delete(@PathParam("email") String email) throws
            FirestoreProxyException;

}
