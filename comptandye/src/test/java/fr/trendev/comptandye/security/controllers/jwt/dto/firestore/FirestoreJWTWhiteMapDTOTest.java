/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

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

//    @Before
//    public void init() {
//        this.dto = new FirestoreJWTWhiteMapDTO();
//        this.dto.init();
//    }
    @Test
    public void testConstructor() {
        Assertions.assertDoesNotThrow(() -> {
            new FirestoreJWTWhiteMapDTO();
        });
    }

    @Test
    public void testInit() {
        Assertions.assertDoesNotThrow(() -> {
            FirestoreJWTWhiteMapDTO tmpDTO = new FirestoreJWTWhiteMapDTO();
            tmpDTO.init();
        });
    }

    @Test
    public void testClose() {
    }

    @Test
    public void testGetAll() {
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
