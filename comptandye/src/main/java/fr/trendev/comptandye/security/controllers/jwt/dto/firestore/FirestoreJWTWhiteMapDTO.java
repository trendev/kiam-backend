/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    private InputStream serviceAccount;
    private Firestore db;
    private static final Logger LOG = Logger
            .getLogger(FirestoreJWTWhiteMapDTO.class.getName());

    @Override
    public void init() {
        try {
            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            serviceAccount = classloader.getResourceAsStream(
                    "comptandye-4c50d-firebase-adminsdk-t7vpe-d96038b6f1.json");

            FirestoreOptions options =
                    FirestoreOptions.newBuilder()
                            .setCredentials(GoogleCredentials.fromStream(
                                    serviceAccount))
                            .setTimestampsInSnapshotsEnabled(true)
                            .build();
            Firestore db = options.getService();

            // asynchronously retrieve all users
            ApiFuture<QuerySnapshot> query = db.collection("users").get();
// ...
// query.get() blocks on response
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                LOG.info("User: " + document.getId());
                LOG.info("First: " + document.getString("first"));
                if (document.contains("middle")) {
                    System.out.
                            println("Middle: " + document.getString("middle"));
                }
                LOG.info("Last: " + document.getString("last"));
                LOG.info("Born: " + document.getLong("born"));
            }

        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Impossible to init "
                    + FirestoreJWTWhiteMapDTO.class.getSimpleName(), ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        LOG.info(FirestoreJWTWhiteMapDTO.class.getSimpleName() + " initialized");
    }

    @Override
    public void close() {
        if (this.db != null && this.serviceAccount != null) {
            try {
//                this.db.close();
                serviceAccount.close();
                LOG.info(FirestoreJWTWhiteMapDTO.class.getSimpleName()
                        + " closed");
            } catch (Exception ex) {
                LOG.
                        log(Level.SEVERE,
                                "Impossible to close the firestore db or the InputStream!!!",
                                ex);
            }
        }
    }

}
