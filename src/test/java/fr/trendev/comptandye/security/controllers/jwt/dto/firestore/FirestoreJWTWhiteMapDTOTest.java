/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTOTest {

    private FirestoreJWTWhiteMapDTO dto;

    public FirestoreJWTWhiteMapDTOTest() {
    }

    @Before
    public void init() {
        this.dto = new FirestoreJWTWhiteMapDTO();
        this.dto.init();
    }

    @Test
    public void testInit() {
        FirestoreJWTWhiteMapDTO dto2 = new FirestoreJWTWhiteMapDTO();
        Assertions.assertDoesNotThrow(dto2::init,
                "DTO initialization should not throw an Exception");
    }

    @Test
    public void testClose() {
        //nothing to test at the present time
    }

    @Test
    public void testGetAll() {

//        try {
//            CompletableFuture<List<JWTWhiteMapEntry>> asyncResponse =
//                    this.dto.getAll()
//                            .toCompletableFuture();
//            List<JWTWhiteMapEntry> list = asyncResponse.get(1000,
//                    TimeUnit.MILLISECONDS);
//            Assertions.assertTrue(asyncResponse.isDone());
//            Assertions.assertFalse(asyncResponse.isCompletedExceptionally());
//        } catch (InterruptedException ex) {
//            throw new AssertionError(ex);
//        } catch (ExecutionException ex) {
//            throw new AssertionError(ex);
//        } catch (TimeoutException ex) {
//            throw new AssertionError(ex);
//        }
    }

    @Test
    public void testBulkUpdates() {
    }

    @Test
    public void testBulkRemoves() {
    }

    @Test
    public void testCreate() {
    }

    @Test
    public void testUpdate() {
    }

    @Test
    public void testDelete() {
    }

}
