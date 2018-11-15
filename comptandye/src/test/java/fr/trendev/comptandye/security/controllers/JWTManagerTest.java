/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author jsie
 */
public class JWTManagerTest {

    private KeyProvider keyProvider;
    private JWTManager jwtManager;

    private final String caller = "julien.sie@gmail.com";
    private final List<String> groups;

    public JWTManagerTest() {
        this.groups = Arrays.asList(new String[]{"Professional",
            "Administrator"});
    }

    @Before
    public void init() {
        this.keyProvider = new KeyProvider();
        this.keyProvider.init();
        this.jwtManager = new JWTManager(this.keyProvider.getPrivateKey(),
                this.keyProvider.getPublicKey());
    }

    @Test
    public void testGenerateToken() {
        try {
            String token = this.jwtManager.generateToken(this.caller, groups);
            Assertions.assertNotEquals(token.length(), 0);
            Assertions.assertNotNull(token);

            SignedJWT parsedJWT = SignedJWT.parse(token);
            Assertions.assertNotNull(parsedJWT);

//            RSAPublicKey publicKey = this.jwtManager.readPublicKey(
//                    "publicKey.pem");
//            JWSVerifier verifier = new RSASSAVerifier(publicKey);
//
//            Assertions.assertTrue(parsedJWT.verify(verifier));
//            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getJWTID(),
//                    this.jti);
//            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getIssuer(),
//                    this.iss);
//            Assertions.assertEquals(parsedJWT.getJWTClaimsSet().getSubject(),
//                    this.sub);
//            Assertions.assertTrue(
//                    (this.iat.getTime() / 1000)
//                    == (parsedJWT.getJWTClaimsSet().getIssueTime()
//                            .getTime() / 1000));
//            Assertions.assertEquals(this.sub, parsedJWT.getJWTClaimsSet().
//                    getStringClaim("upn"));
//            Assertions.assertIterableEquals(this.groups, parsedJWT.
//                    getJWTClaimsSet().
//                    getStringListClaim("groups"));
        } catch (KeyLengthException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (JOSEException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(JWTManagerTest.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
    }

}
