/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class JWTWhiteMapDTODTOProducer {

    @Produces
    @FirestoreDTO
    public JWTWhiteMapDTO getFirstoreJWTWhiteMapDTO() {
        return null;
    }

    @Produces
    @DynamodbDTO
    public JWTWhiteMapDTO getDynamodbJWTWhiteMapDTO() {
        return null;
    }

}
