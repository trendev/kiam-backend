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
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public final static int SHORT_VALID_PERIOD = 30;
    public final static TemporalUnit SHORT_VALID_PERIOD_UNIT = ChronoUnit.MINUTES;
    public final static int LONG_VALID_PERIOD = 2;
    public final static TemporalUnit LONG_VALID_PERIOD_UNIT = ChronoUnit.MONTHS;

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

    @Inject
    private JWTRevokedSet jwtRevokedSet;

    private final ScheduledExecutorService scheduler;

    public JWTManager() {
        this.scheduler = Executors.newScheduledThreadPool(2);
    }

    @PostConstruct
    public void init() {
        this.signer = new RSASSASigner(this.privateKey);
        this.verifier = new RSASSAVerifier(this.publicKey);
    }

    public static String trunkToken(String token) {
        int l = token.length();
        int n = 16;
        return l < n ? token : "..." + token.substring(l - n, l);
    }

    public JWTWhiteMap getWhiteMap() {
        return jwtWhiteMap;
    }

    public JWTRevokedSet getRevokedSet() {
        return jwtRevokedSet;
    }

    //rename generateNewToken
    public String generateToken(final String caller,
            final List<String> groups,
            final String xsrf,
            final boolean rmbme)
            throws JOSEException {
        Instant currentTime = Instant.now();
        Instant expirationTime = currentTime.plus(SHORT_VALID_PERIOD,
                SHORT_VALID_PERIOD_UNIT);

        final String jti = UUID.randomUUID().toString();

        JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder();
        claimSetBuilder.issuer(ISS);
        claimSetBuilder.subject(caller);
        claimSetBuilder.issueTime(Date.from(currentTime));
        claimSetBuilder.expirationTime(Date.from(expirationTime));
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
                Date.from(currentTime),
                Date.from(expirationTime)));

        // auto-removes the expired tokens from the JWT White Map
        scheduler.schedule(() -> {
            jwtWhiteMap.remove(caller, token)
                    .ifPresent(r -> LOG.log(Level.INFO,
                            "Token of user [{0}] ({1}) has expired...",
                            new Object[]{
                                caller,
                                trunkToken(r.getToken())
                            }));
        },
                expirationTime.toEpochMilli() - System.currentTimeMillis(),
                TimeUnit.MILLISECONDS);

        return token;
    }

    public Optional<JWTClaimsSet> extractClaimsSet(String token) {
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

    public boolean isExpired(final JWTClaimsSet claims) {
        Instant now = Instant.now();
        Instant exp = claims.getExpirationTime().toInstant();
        return now.isAfter(exp);
    }

    public boolean isRevoked(final String token) {
        return this.jwtRevokedSet.contains(token);
    }

    public boolean canBeRefreshed(final JWTClaimsSet claims) {
        Instant now = Instant.now();
        Instant issueTime = claims.getIssueTime().toInstant();
        Instant expirationTime = claims.getExpirationTime().toInstant();

        return now.isAfter(
                issueTime.plusMillis(
                        (expirationTime.toEpochMilli()
                        - issueTime.toEpochMilli()) / 2
                ));
    }

    //TODO : implement + test
    public Optional<JWTRecord> revokeToken(final String email,
            final String token) {
        Optional<JWTRecord> record = this.jwtWhiteMap.remove(email, token);
        record.ifPresent(r -> {
            if (this.jwtRevokedSet.add(r)) {
                LOG.log(Level.WARNING, "Token (" + trunkToken(token)
                        + ") has been REVOKED and addded in JWT RevokedSet");

                // auto-removes the expired tokens from the JWT Revoked List
                scheduler.schedule(() -> {
                    jwtRevokedSet.remove(token)
                            .ifPresent(r_ -> LOG.log(Level.INFO,
                                    "Token of user [{0}] ({1}) has expired...",
                                    new Object[]{
                                        email,
                                        trunkToken(token)
                                    }));
                },
                        r.getExpirationDate().getTime() - System.
                        currentTimeMillis(),
                        TimeUnit.MILLISECONDS);
            }
        });
        return record;
    }

    public Optional<Set<JWTRecord>> revokeAllTokens(final String email) {
        return null;
    }

    //TODO : implement + test
    public String signClaimsSet(final JWTClaimsSet claimsSet) {
        return null;
    }

    //TODO : implement + test
    public String refreshToken(final JWTClaimsSet claimsSet) {
        return null;
    }

    //TODO : implement + test
    public JWTClaimsSet createClaimsSet() {
        return null;
    }

}
