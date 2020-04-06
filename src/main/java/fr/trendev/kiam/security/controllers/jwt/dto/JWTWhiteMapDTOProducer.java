/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers.jwt.dto;

import fr.trendev.kiam.security.controllers.jwt.dto.dynamodb.DynamodbDTO;
import fr.trendev.kiam.security.controllers.jwt.dto.firestore.FirestoreDTO;
import fr.trendev.kiam.security.controllers.jwt.dto.firestore.FirestoreJWTWhiteMapDTO;
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
public class JWTWhiteMapDTOProducer {

    private JWTWhiteMapDTO firestoreDTO;

    private static final Logger LOG = Logger
            .getLogger(JWTWhiteMapDTOProducer.class.getName());

    @Produces
    @Default
    @FirestoreDTO
    public JWTWhiteMapDTO getFirestoreJWTWhiteMapDTO() {
        if (this.firestoreDTO == null) {
            this.firestoreDTO = new FirestoreJWTWhiteMapDTO();
            this.firestoreDTO.init();
        }
        return this.firestoreDTO;
    }

    @Produces
    @DynamodbDTO
    public JWTWhiteMapDTO getDynamodbJWTWhiteMapDTO() {
        throw new UnsupportedOperationException(
                "Cannot produce DynamoDB DTO for the JWT White Map");
    }

    @PreDestroy
    public void close() {
        if (this.firestoreDTO != null) {
            this.firestoreDTO.close();
        }
        LOG.log(Level.INFO, "{0} is now closed",
                JWTWhiteMapDTOProducer.class.getSimpleName());
    }

}
