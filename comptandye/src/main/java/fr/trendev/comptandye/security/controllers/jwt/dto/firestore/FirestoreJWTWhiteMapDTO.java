/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto.firestore;

import fr.trendev.comptandye.security.controllers.jwt.dto.JWTWhiteMapDTO;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

//    private Firestore db;
    private static final Logger LOG = Logger
            .getLogger(FirestoreJWTWhiteMapDTO.class.getName());

    @Override
    public void init() {
        /*
        InputStream serviceAccount = null;
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
        } finally {
            try {
                if (db != null && serviceAccount != null) {
                    db.close();
                    serviceAccount.close();
                }
            } catch (Exception ex) {
                throw new IllegalStateException(FirestoreJWTWhiteMapDTO.class.
                        getSimpleName()
                        + " cannot be initialized and InputStream cannot be closed");
            }
        }
        LOG.info(FirestoreJWTWhiteMapDTO.class.getSimpleName() + " initialized");
         */
    }

    @Override
    public void close() {
        /*
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
         */
    }

}
