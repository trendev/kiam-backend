/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto.firestore;

import fr.trendev.kiam.security.entities.JWTRecord;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.SocketException;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
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
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                SocketException.class,
                ProcessingException.class
            }
    )
    CompletionStage<Set<JWTRecord>> getAll();

    @POST
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                SocketException.class,
                ProcessingException.class
            }
    )
    CompletionStage<JWTRecord> create(JWTRecord record);

    @POST
    @Path("bulk-creation")
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                SocketException.class,
                ProcessingException.class
            }
    )
    CompletionStage<Set<JWTRecord>> bulkCreation(Set<JWTRecord> records);

    @DELETE
    @Path("{token}")
    @Retry(
            maxRetries = 10,
            delay = 200L,
            delayUnit = ChronoUnit.MILLIS,
            jitter = 50,
            abortOn = {ConnectException.class},
            retryOn = {
                WebApplicationException.class,
                SocketException.class,
                ProcessingException.class
            }
    )
    CompletionStage<String> delete(@PathParam("token") String token);

}
