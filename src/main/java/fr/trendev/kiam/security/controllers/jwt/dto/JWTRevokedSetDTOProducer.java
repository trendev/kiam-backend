/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto;

import fr.trendev.kiam.security.controllers.jwt.dto.dynamodb.DynamodbDTO;
import fr.trendev.kiam.security.controllers.jwt.dto.firestore.FirestoreDTO;
import fr.trendev.kiam.security.controllers.jwt.dto.firestore.FirestoreJWTRevokedSetDTO;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class JWTRevokedSetDTOProducer {

    private JWTRevokedSetDTO firestoreDTO;

    private static final Logger LOG = Logger.getLogger(
            JWTRevokedSetDTOProducer.class.getName());

    @Produces
    @Default
    @FirestoreDTO
    public JWTRevokedSetDTO getFirestoreJWTRevokedSetDTO() {
        if (this.firestoreDTO == null) {
            this.firestoreDTO = new FirestoreJWTRevokedSetDTO();
            this.firestoreDTO.init();
        }
        return this.firestoreDTO;
    }

    @Produces
    @DynamodbDTO
    public JWTRevokedSetDTO getDynamodbJWTRevokedSetDTO() {
        throw new UnsupportedOperationException(
                "Cannot produce DynamoDB DTO for the JWT Revoked Set");
    }

    @PreDestroy
    public void close() {
        if (this.firestoreDTO != null) {
            this.firestoreDTO.close();
        }
        LOG.log(Level.INFO, "{0} is now closed",
                JWTRevokedSetDTOProducer.class.getSimpleName());
    }

}
