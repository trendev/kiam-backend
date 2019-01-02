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
public class JWTWhiteMapDTOProducer {

    @Produces
    @FirestoreDTO
    public JWTWhiteMapDTO getFirestoreJWTWhiteMapDTO() {
        return new FirestoreJWTWhiteMapDTO();
    }

    @Produces
    @DynamodbDTO
    public JWTWhiteMapDTO getDynamodbJWTWhiteMapDTO() {
        throw new UnsupportedOperationException(
                "Cannot produce DynamoDB DTO for the JWT White Map");
    }

}
