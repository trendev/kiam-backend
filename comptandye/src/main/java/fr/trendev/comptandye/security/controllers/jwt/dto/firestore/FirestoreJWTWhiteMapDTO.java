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
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    private static final Logger LOG = Logger
            .getLogger(FirestoreJWTWhiteMapDTO.class.getName());

    @Override
    public void init() {
        try {
            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            InputStream serviceAccount = classloader.getResourceAsStream(
                    "comptandye-4c50d-firebase-adminsdk-t7vpe-d96038b6f1.json");

            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setFirestoreOptions(FirestoreOptions.newBuilder()
                            .setCredentials(credentials)
                            .setTimestampsInSnapshotsEnabled(true)
                            .build())
                    .setCredentials(credentials)
                    .build();

            FirebaseApp fa = FirebaseApp.initializeApp(options);

            Firestore firestore = FirestoreClient.getFirestore();

            // asynchronously retrieve all users
            ApiFuture<QuerySnapshot> query = firestore.collection("users").get();
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

//            this.firestore.close();
//            serviceAccount.close();
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Impossible to init "
                    + FirestoreJWTWhiteMapDTO.class.getSimpleName(), ex);
        }
        LOG.info(FirestoreJWTWhiteMapDTO.class.getSimpleName()
                + " initialized");
    }

}
