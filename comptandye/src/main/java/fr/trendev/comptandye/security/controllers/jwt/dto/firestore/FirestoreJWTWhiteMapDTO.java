/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    private Firestore db;

    private static final Logger LOG = Logger
            .getLogger(FirestoreJWTWhiteMapDTO.class.getName());

    @Override
    public void init() {
        InputStream serviceAccount = null;
        try {
            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            serviceAccount = classloader.getResourceAsStream(
                    "service-account-key.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            this.db = FirestoreClient.getFirestore();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Impossible to init "
                    + FirestoreJWTWhiteMapDTO.class.getSimpleName(), ex);
        } finally {
            try {
                serviceAccount.close();
            } catch (IOException ex) {
                throw new IllegalStateException(FirestoreJWTWhiteMapDTO.class.
                        getSimpleName()
                        + " cannot be initialized and InputStream cannot be closed");
            }
        }
        LOG.info(FirestoreJWTWhiteMapDTO.class.getSimpleName() + " initialized");
    }

    @Override
    public void close() {
        if (this.db != null) {
            try {
                this.db.close();
                LOG.info(FirestoreJWTWhiteMapDTO.class.getSimpleName()
                        + " closed");
            } catch (Exception ex) {
                LOG.
                        log(Level.SEVERE,
                                "Impossible to close the firestore db !!!",
                                ex);
            }
        }
    }

}
