/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto;

import fr.trendev.comptandye.security.controllers.jwt.dto.dynamodb.DynamodbDTO;
import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.FirestoreDTO;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class JWTRevokedSetDTOProducer {

    @Produces
    @FirestoreDTO
    public JWTRevokedSetDTO getFirestoreJWTRevokedSetDTO() {
        return null;
    }

    @Produces
    @DynamodbDTO
    public JWTRevokedSetDTO getDynamodbJWTRevokedSetDTO() {
        throw new UnsupportedOperationException(
                "Cannot produce DynamoDB DTO for the JWT Revoked Set");
    }

}
