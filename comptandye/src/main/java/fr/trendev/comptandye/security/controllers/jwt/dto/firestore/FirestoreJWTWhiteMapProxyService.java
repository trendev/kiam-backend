/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
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

/**
 *
 * @author jsie
 */
@Path("jwtwhitemap")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface FirestoreJWTWhiteMapProxyService {

    /**
     * Indirectly used by JWTWhiteMap: should block until JWTWhiteMap is fully
     * loaded (are not)
     */
    @GET
    List<JWTWhiteMapEntry> getAll();

    @PUT
    @Path("updates")
    CompletionStage<Void> bulkUpdates(List<JWTWhiteMapEntry> dtoUpdates);

    @DELETE
    @Path("removes")
    CompletionStage<Void> bulkRemoves(List<String> dtoRemoves);

    //TODO : split into create/update
    @POST
    CompletionStage<Void> save(JWTWhiteMapEntry jwtWhiteMapEntry);

    @DELETE
    @Path("{email}")
    CompletionStage<Void> remove(@PathParam("email") String email);

}
