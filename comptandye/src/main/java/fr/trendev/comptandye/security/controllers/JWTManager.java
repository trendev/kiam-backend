/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import com.nimbusds.jose.JOSEException;
import static com.nimbusds.jose.JOSEObjectType.JWT;
import static com.nimbusds.jose.JWSAlgorithm.RS256;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class JWTManager {

    @Inject
    private PrivateKey privateKey;

    @Inject
    private RSAPublicKey publicKey;

    public JWTManager() {
    }

    public JWTManager(final PrivateKey privateKey, final RSAPublicKey publicKey) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public final static String ISS = "https://www.comptandye.fr";

    public final static int VALID_PERIOD = 3;

    private static final Logger LOG = Logger.getLogger(
            JWTManager.class.getName());

    public String generateToken(final String caller, final List<String> groups)
            throws JOSEException {
        Instant current_time = Instant.now();
        Instant expiration_time = current_time.plus(VALID_PERIOD,
                ChronoUnit.MINUTES);

        final String jti = UUID.randomUUID().toString();

        JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();
        claimSetBuilder.issuer(ISS);
        claimSetBuilder.subject(caller);
        claimSetBuilder.issueTime(Date.from(current_time));
        claimSetBuilder.expirationTime(Date.from(expiration_time));
        claimSetBuilder.jwtID(jti);

        //MP-JWT specific
        claimSetBuilder.claim("upn", caller);
        claimSetBuilder.claim("groups", groups);

        JWTClaimsSet claimsSet = claimSetBuilder.build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(RS256)
                        .keyID("privateKey.pem")
                        .type(JWT)
                        .build(), claimsSet);

        signedJWT.sign(new RSASSASigner(this.privateKey));

        String token = signedJWT.serialize();
        LOG.log(Level.INFO, "JWT generated for user {0} :\n{1}\njti = {2}",
                new Object[]{caller, token, jti});
        return token;
    }

}
