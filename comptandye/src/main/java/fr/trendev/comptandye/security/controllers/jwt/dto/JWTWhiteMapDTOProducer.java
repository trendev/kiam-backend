/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto;

import fr.trendev.comptandye.security.controllers.jwt.dto.dynamodb.DynamodbDTO;
import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.FirestoreJWTWhiteMapDTO;
import fr.trendev.comptandye.security.controllers.jwt.dto.firestore.FirestoreDTO;
import java.util.logging.Logger;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class JWTWhiteMapDTOProducer {

    private JWTWhiteMapDTO jwtwmdto;

    private static final Logger LOG = Logger
            .getLogger(JWTWhiteMapDTOProducer.class.getName());

    @Produces
    @FirestoreDTO
    public JWTWhiteMapDTO getFirestoreJWTWhiteMapDTO() {
        if (this.jwtwmdto == null) {
            this.jwtwmdto = new FirestoreJWTWhiteMapDTO();
            this.jwtwmdto.init();
        }
        return this.jwtwmdto;
    }

    @Produces
    @DynamodbDTO
    public JWTWhiteMapDTO getDynamodbJWTWhiteMapDTO() {
        throw new UnsupportedOperationException(
                "Cannot produce DynamoDB DTO for the JWT White Map");
    }

    @PreDestroy
    public void close() {
        this.jwtwmdto.close();
        LOG.info(JWTWhiteMapDTOProducer.class.getSimpleName()
                + " is now closed");
    }

}
