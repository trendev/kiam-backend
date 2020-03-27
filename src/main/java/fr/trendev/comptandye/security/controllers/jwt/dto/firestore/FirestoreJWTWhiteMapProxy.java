/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.exceptions.FirestoreProxyException;
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.io.Serializable;
import java.net.ConnectException;
import java.time.temporal.ChronoUnit;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

/**
 *
 * @author jsie
 */
@Path("jwtwhitemap")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface FirestoreJWTWhiteMapProxy extends Serializable {

    @GET
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                FirestoreProxyException.class
            }
    )
    CompletionStage<List<JWTWhiteMapEntry>> getAll() throws FirestoreProxyException;

    @POST
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                FirestoreProxyException.class
            }
    )
    CompletionStage<JWTWhiteMapEntry> create(JWTWhiteMapEntry jwtWhiteMapEntry) throws FirestoreProxyException;

    @PUT
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                FirestoreProxyException.class
            }
    )
    CompletionStage<JWTWhiteMapEntry> update(JWTWhiteMapEntry jwtWhiteMapEntry) throws FirestoreProxyException;

    @DELETE
    @Path("{email}")
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                FirestoreProxyException.class
            }
    )
    CompletionStage<String> delete(@PathParam("email") String email) throws FirestoreProxyException;

}
