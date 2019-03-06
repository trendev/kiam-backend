/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

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

    static <T> T errorHandler(Throwable ex,
            String message,
            T t,
            Logger LOG) {
        LOG.log(Level.INFO, message, ex);
        LOG.log(Level.WARNING, message);
        return t;
    }

    static final <P, T> void manageSilentOperations(
            P proxy,
            String successMsg,
            String errMsg,
            ThrowingBiFunction<P, T, CompletionStage<Void>> fn,
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
