/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import java.util.function.Predicate;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

/**
 *
 * @author jsie
 */
@Provider
public class FirestoreProxyExceptionMapper implements
        ResponseExceptionMapper<FirestoreProxyException> {

    private final Predicate<Integer> isHttpError =
            status -> status >= 400 || status == 0;

    @Override
    public FirestoreProxyException toThrowable(Response response) {
        int status = response.getStatus();
        if (isHttpError.test(status)) {
            return new FirestoreProxyException(
                    "FirestoreProxyException : Error - HTTP " + status);
        }
        return null;
    }

    @Override
    public boolean handles(int status,
            MultivaluedMap<String, Object> headers) {
        return isHttpError.test(status);
    }

}
