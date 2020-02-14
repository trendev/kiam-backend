/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.AuthenticationEventController;
import fr.trendev.comptandye.security.controllers.SlackAuthenticationEventController;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

/**
 *
 * @author jsie
 */
public class FirestoreJWTDTOHelper {

    private static AuthenticationEventController AEC = new SlackAuthenticationEventController();

    private static final Logger LOG = Logger.getLogger(FirestoreJWTDTOHelper.class.getName());

    static URI loadUri() {

        Config config = ConfigProvider.getConfig();
        String prop = "FIRESTORE_PROXY_URL"; // set from ENV var
        String url = config.getValue(prop, String.class);

        try {
            URI uri = new URI(url);
            LOG.log(Level.INFO, "{0} = \"{1}\"", new Object[]{prop, url});
            return uri;
        } catch (URISyntaxException ex) {
            throw new IllegalStateException(
                    "Firestore proxy URL provided in config properties is not valid", ex);
        }
    }

    /**
     * Programmatically builds an instance of a MicroProfile Rest Client.
     *
     * @param <T> the type of the microprofile rest client
     * @param apiUri the URI of the remote service
     * @param proxyClass the class of the microprofile rest client
     * @return an instance of the microprofile rest client
     */
    static <T> T buildProxy(URI apiUri, Class<T> proxyClass) {
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
     * @return a default response if an error occurs
     */
    static <T> T errorHandler(Throwable ex,
            String message,
            T t) {
        LOG.log(Level.WARNING, message, ex);
        AEC.emitFirestoreIssue(message, ex.getMessage());
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
     */
    static final <P, T> void manageSilentOperations(
            P proxy,
            String successMsg,
            String errMsg,
            ThrowingBiFunction<P, T, CompletionStage<T>> fn,
            T t) {
        try {
            fn.applyThrows(proxy, t)
                    .thenRun(() -> LOG.info(successMsg))
                    .exceptionally(ex -> errorHandler(ex, errMsg, null));
        } catch (Throwable ex) {
            errorHandler(ex, errMsg, null);
        }
    }

}
