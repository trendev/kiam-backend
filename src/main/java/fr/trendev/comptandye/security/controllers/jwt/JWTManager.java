/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers.jwt;

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
import fr.trendev.comptandye.security.entities.JWTWhiteMapEntry;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import static java.time.temporal.ChronoUnit.MILLIS;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collector;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 * @author jsie
 */
@ApplicationScoped
public class JWTManager {

    public final static int SHORT_TERM_VALIDITY = 20;
    public final static TemporalUnit SHORT_TERM_VALIDITY_UNIT = ChronoUnit.MINUTES;
    public final static int LONG_TERM_VALIDITY = 60;
    public final static TemporalUnit LONG_TERM_VALIDITY_UNIT = ChronoUnit.DAYS;
    public final static int SLOT = 8;

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

    @PostConstruct
    public void init() {
        this.signer = new RSASSASigner(this.privateKey);
        this.verifier = new RSASSAVerifier(this.publicKey);
    }

    public static String trunkToken(String token) {
        if (token == null) {
            throw new IllegalArgumentException(
                    "token is null and cannot be trunked");
        }
        int l = token.length();
        int n = 16;
        return l < n ? token : "..." + token.substring(l - n, l);
    }

    public JWTWhiteMap getJWTWhiteMap() {
        return jwtWhiteMap;
    }

    public Set<JWTWhiteMapEntry> getJWTWhiteMapEntries() {
        return jwtWhiteMap.getMap().entrySet().stream()
                .map(JWTWhiteMapEntry::new)
                .collect(Collector.of(TreeSet::new,
                        TreeSet::add,
                        (l, r) -> {
                    l.addAll(r);
                    return l;
                }));
    }

    public JWTRevokedSet getJWTRevokedSet() {
        return jwtRevokedSet;
    }

    private String generateToken(
            String caller,
            Instant currentTime,
            Instant expirationTime,
            JWTClaimsSet.Builder csbuilder,
            String logFormat) throws JOSEException {

        csbuilder.issueTime(Date.from(currentTime));
        csbuilder.expirationTime(Date.from(expirationTime));

        String token = this.signClaimsSet(csbuilder.build());

        LOG.log(Level.INFO, String.format(logFormat, caller, token));

        jwtWhiteMap.add(caller, new JWTRecord(token,
                Date.from(currentTime),
                Date.from(expirationTime)));

        return token;
    }

    public String createToken(final String caller,
            final List<String> groups,
            final String xsrf,
            final boolean rmbme)
            throws JOSEException {

        Instant currentTime = Instant.now();

        return this.generateToken(caller,
                currentTime,
                currentTime.plus(
                        rmbme ? LONG_TERM_VALIDITY : SHORT_TERM_VALIDITY,
                        rmbme ? LONG_TERM_VALIDITY_UNIT : SHORT_TERM_VALIDITY_UNIT),
                this.createClaimsSetBuilder(
                        caller,
                        groups,
                        xsrf,
                        0),
                "JWT created for user %1$s :\n%2$s");
    }

    public String refreshToken(final JWTClaimsSet cs) throws ParseException,
            JOSEException {

        final String caller = cs.getSubject();

        Instant now = Instant.now();

        return this.generateToken(caller,
                now,
                now.plus(
                        cs.getExpirationTime().getTime()
                        - cs.getIssueTime().getTime(),
                        MILLIS),
                this.createClaimsSetBuilder(
                        caller,
                        cs.getStringListClaim("groups"),
                        cs.getStringClaim("xsrf"),
                        cs.getIntegerClaim("refresh") + 1),
                "JWT refreshed for user %1$s :\n%2$s");
    }

    private JWTClaimsSet.Builder createClaimsSetBuilder(final String caller,
            final List<String> groups,
            final String xsrf,
            final int refresh) {

        JWTClaimsSet.Builder csbuilder = new JWTClaimsSet.Builder();

        csbuilder.issuer(ISS);
        csbuilder.subject(caller);
        csbuilder.jwtID(UUID.randomUUID().toString());

        //MP-JWT specific
        csbuilder.claim("upn", caller);
        csbuilder.claim("groups", groups);

        //XSRF-TOKEN
        csbuilder.claim("xsrf", xsrf);

        //Renewal occurency
        csbuilder.claim("refresh", refresh);

        return csbuilder;
    }

    private String signClaimsSet(final JWTClaimsSet claimsSet) throws
            JOSEException {
        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(RS256)
                        .keyID("privateKey.pem")
                        .type(JWT)
                        .build(), claimsSet);

        signedJWT.sign(this.signer);

        return signedJWT.serialize();
    }

    public Optional<JWTClaimsSet> extractClaimsSet(String token) {
        try {
            if (token != null && !token.isEmpty()) {
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

    /**
     * Checks in the revoked set if a JWT is revoked
     *
     * @param token the token to control
     * @return true if the token has been revoked, false otherwise
     */
    public boolean isRevoked(final String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException(
                    "JWT token cannot be null or empty");
        }
        return this.jwtRevokedSet.contains(token);
    }

    /**
     * Checks if a token can be refreshed
     *
     * @param claims the claims associated to the token (JWT)
     * @return true if the token can be refreshed, false otherwise
     */
    public boolean canBeRefreshed(final JWTClaimsSet claims) {
        Instant now = Instant.now();
        Instant iat = claims.getIssueTime().toInstant();
        Instant exp = claims.getExpirationTime().toInstant();
        Instant refreshZone = this.refreshZone(iat, exp);

        return now.equals(refreshZone) || now.isAfter(refreshZone);
    }

    /**
     * Computes the refresh zone between the issue time and the expiration time.
     * The difference between the issue time and the expiration time is split
     * into slots and the refresh zone matches the last slot.
     *
     * @param iat the issue time
     * @param exp the expiration time
     * @return the Instant when the refresh zone starts
     */
    public Instant refreshZone(Instant iat, Instant exp) {
        return exp.minusMillis((exp.toEpochMilli() - iat.toEpochMilli()) / SLOT);
    }

    public Optional<JWTRecord> revokeToken(final String email,
            final String token) {
        Optional<JWTRecord> record = this.jwtWhiteMap.remove(email, token);
        record.ifPresent(r -> {
            if (this.jwtRevokedSet.add(r)) {
                LOG.log(Level.WARNING,
                        "Token ({0}) has been REVOKED and added in JWT RevokedSet",
                        trunkToken(token));
            }
        });
        return record;
    }

    public Optional<Set<JWTRecord>> revokeAllTokens(final String email) {
        Optional<Set<JWTRecord>> records = this.jwtWhiteMap.removeAll(email);
        records.ifPresent(rs -> {
            if (this.jwtRevokedSet.addAll(rs)) {
                LOG.log(Level.WARNING,
                        "All Tokens of user [{0}] have been REVOKED and added in JWT RevokedSet",
                        email);
            }
        });
        return records;
    }

}
