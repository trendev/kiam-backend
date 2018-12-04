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
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import fr.trendev.comptandye.security.entities.JWTRecord;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class JWTManager {

    public final static int VALID_PERIOD = 1;

    public final static String ISS = "https://www.comptandye.fr";

    private static final Logger LOG = Logger.getLogger(
            JWTManager.class.getName());

    @Inject
    private PrivateKey privateKey;

    @Inject
    private RSAPublicKey publicKey;

    private JWSSigner signer;
    private JWSVerifier verifier;

    @Inject
    private JWTWhiteMap jwtWhiteMap;

    private final ScheduledExecutorService scheduler;

    public JWTManager() {
        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    @PostConstruct
    public void init() {
        this.signer = new RSASSASigner(this.privateKey);
        this.verifier = new RSASSAVerifier(this.publicKey);
    }

    public JWTWhiteMap getWhiteMap() {
        return this.jwtWhiteMap;
    }

    public String generateToken(final String caller,
            final List<String> groups,
            final String xsrf,
            final boolean rmbme)
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

        //XSRF-TOKEN
        claimSetBuilder.claim("xsrf", xsrf);

        JWTClaimsSet claimsSet = claimSetBuilder.build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(RS256)
                        .keyID("privateKey.pem")
                        .type(JWT)
                        .build(), claimsSet);

        signedJWT.sign(this.signer);

        String token = signedJWT.serialize();
        LOG.log(Level.INFO,
                "JWT generated for user {0} :\n{1}\njti = {2}\nxsrf = {3}",
                new Object[]{caller, token, jti, xsrf});

        jwtWhiteMap.add(caller, new JWTRecord(token,
                Date.from(current_time),
                Date.from(expiration_time)));

        // auto-removes the expired tokens from the JWT White Map
        scheduler.schedule(() -> jwtWhiteMap.remove(caller, token),
                expiration_time.toEpochMilli() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS);

        return token;
    }

    public Optional<JWTClaimsSet> getClaimsSet(String token) {
        try {
            if (token != null) {
                SignedJWT parsedJWT = SignedJWT.parse(token);

                boolean verified = parsedJWT.verify(this.verifier);

                if (verified) {
                    return Optional.of(parsedJWT.getJWTClaimsSet());
                } else {
                    LOG.log(Level.WARNING, "** INVALID JWT SIGNATURE **");
                }
            }
        } catch (ParseException ex) {
            LOG.log(Level.WARNING, "Provided JWT {0} cannot be parsed !!!",
                    token);
        } catch (JOSEException ex) {
            LOG.log(Level.SEVERE,
                    "Provided JWT {0} signature CANNOT BE VERIFIED !!!", token);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error processing the JWT " + token, ex);
        }

        // JWT IS INVALID
        return Optional.empty();
    }

    public boolean hasExpired(final JWTClaimsSet claims) {
        Instant now = Instant.now();
        Instant exp = claims.getExpirationTime().toInstant();
        return now.isAfter(exp);
    }

    public boolean canBeRefreshed(final JWTClaimsSet claims) {
        Instant now = Instant.now();
        // TODO : check if the token will expire soon and must be refreshed
        return false;
    }

    public static String trunkToken(String token) {
        int l = token.length();
        int n = 10;
        return l < n ? token : "..." + token.substring(l - n, l);
    }

}
