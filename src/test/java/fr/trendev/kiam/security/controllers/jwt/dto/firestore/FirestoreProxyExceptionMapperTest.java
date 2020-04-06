/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.exceptions.FirestoreProxyExceptionMapper;
import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.exceptions.FirestoreProxyException;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class FirestoreProxyExceptionMapperTest {

    private final FirestoreProxyExceptionMapper mapper;

    public FirestoreProxyExceptionMapperTest() {
        this.mapper = new FirestoreProxyExceptionMapper();
    }

    @Test
    public void testToThrowable() {

        Assertions.assertNull(mapper.toThrowable(Response.ok().build()));
        Assertions.assertNull(mapper.toThrowable(Response.status(
                Response.Status.MOVED_PERMANENTLY).build()));

        Exception ex = mapper.toThrowable(Response.status(
                Response.Status.NOT_FOUND).build());

        Assertions.assertNotNull(ex);
        Assertions.assertTrue(ex instanceof FirestoreProxyException);
        Assertions.assertEquals("FirestoreProxyException : Error - HTTP "
                + Response.Status.NOT_FOUND.getStatusCode(),
                ex.getMessage());

        ex = mapper.toThrowable(Response.status(Response.Status.BAD_REQUEST).
                build());
        Assertions.assertNotNull(ex);
        Assertions.assertTrue(ex instanceof FirestoreProxyException);
        Assertions.assertEquals("FirestoreProxyException : Error - HTTP "
                + Response.Status.BAD_REQUEST.getStatusCode(),
                ex.getMessage());

    }

    @Test
    public void testHandles() {
        Assertions.assertFalse(mapper.handles(
                Response.Status.CREATED.getStatusCode(), null));
        Assertions.assertFalse(mapper.handles(
                Response.Status.MOVED_PERMANENTLY.getStatusCode(), null));
        Assertions.assertTrue(mapper.handles(0, null));
        Assertions.assertTrue(mapper.handles(
                Response.Status.NOT_FOUND.getStatusCode(), null));
    }

}
