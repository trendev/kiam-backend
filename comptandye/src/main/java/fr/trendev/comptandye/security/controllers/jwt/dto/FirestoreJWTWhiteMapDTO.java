/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt.dto;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class FirestoreJWTWhiteMapDTO implements JWTWhiteMapDTO {

    public FirestoreJWTWhiteMapDTO() {
        InputStream serviceAccount = null;
        try {
            serviceAccount = new FileInputStream(
                    "comptandye-4c50d-firebase-adminsdk-t7vpe-ab86c61104.json");
            GoogleCredentials credentials = GoogleCredentials.fromStream(
                    serviceAccount);
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            Firestore db = FirestoreClient.getFirestore();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName()).
                    log(Level.SEVERE, null, ex);
        } finally {
            try {
                serviceAccount.close();
            } catch (IOException ex) {
                Logger.getLogger(FirestoreJWTWhiteMapDTO.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        }
    }
}
