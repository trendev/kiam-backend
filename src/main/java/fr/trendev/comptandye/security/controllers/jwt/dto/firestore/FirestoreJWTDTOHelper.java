/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.AuthenticationEventController;
import fr.trendev.comptandye.security.controllers.SlackAuthenticationEventController;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

/**
 *
 * @author jsie
 */
public class FirestoreJWTDTOHelper {

    private static AuthenticationEventController AEC = new SlackAuthenticationEventController();

    /**
     * Creates an URI from the property of a local property file. The property
     * file should be managed by environments.
     *
     * @param LOG the logger of method caller
     * @param prop the property to get from the property file
     * @return the build URI
     */
    static URI loadUri(Logger LOG, String prop) {

        ClassLoader classloader = Thread.currentThread().
                getContextClassLoader();

        try (InputStream is = classloader.getResourceAsStream(
                "firestore/firestore.properties")) {

            Properties properties = new Properties();
            properties.load(is);

            // loads the properties
            String url = properties.getProperty(prop);

            LOG.log(Level.INFO, "{0} = \"{1}\"", new Object[]{prop, url});

            return new URI(url);
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(
                    "Url provided in properties is not valid", ex);
        } catch (IOException ex) {
            throw new IllegalStateException(
                    "IO Errors setting Firestore properties", ex);
        }
    }

    /**
     * Programmatically builds an instance of a MicroProfile Rest Client.
     *
     * @param <T> the type of the microprofile rest client
     * @param apiUri the URI of the remote service
     * @param proxyClass the class of the microprofile rest client
     * @param LOG the logger
     * @return an instance of the microprofile rest client
     */
    static <T> T buildProxy(URI apiUri, Class<T> proxyClass, Logger LOG) {
        T proxy = RestClientBuilder.newBuilder()
                .baseUri(apiUri)
                .register(FirestoreProxyExceptionMapper.class)
                .build(proxyClass);
        LOG.log(Level.INFO, "New instance of "
                + proxyClass.getSimpleName()
                + " created");
        return proxy;
    }

    /**
     * Logs the Exception and sends in Slack
     *
     * @param <T> the type of the returned object
     * @param ex the exception to log
     * @param message the error message
     * @param t the object to return, can be null
     * @param LOG the logger of the method caller
     * @return a default response if an error occurs
     */
    static <T> T errorHandler(Throwable ex,
            String message,
            T t,
            Logger LOG) {
        LOG.log(Level.WARNING, message, ex);
        AEC.postFirestoreIssue(message, ex.getMessage());
        return t;
    }

    /**
     * Manages silent operations of the rest client implementation.
     *
     * @param <P> the type of the microprofile rest client
     * @param <T> the type of the response
     * @param proxy the microprofile rest client, used a classic proxy
     * @param successMsg the success message
     * @param errMsg the error message
     * @param fn the method to call on the microprofile rest client
     * @param t the response object
     * @param LOG the logger
     */
    static final <P, T> void manageSilentOperations(
            P proxy,
            String successMsg,
            String errMsg,
            ThrowingBiFunction<P, T, CompletionStage<T>> fn,
            T t,
            Logger LOG) {
        try {
            fn.applyThrows(proxy, t)
                    .thenRun(() -> LOG.info(successMsg))
                    .exceptionally(ex -> errorHandler(ex, errMsg, null, LOG));
        } catch (Throwable ex) {
            errorHandler(ex, errMsg, null, LOG);
        }
    }

}
