/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fr.trendev.comptandye.security.controllers.jwt.JWTManager;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
@DeclareRoles({
    "Administrator",
    "Professional",
    "Individual"
})
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/MySQLDataSourceComptaNdye",
        callerQuery = "select PASSWORD from USER_ACCOUNT where EMAIL=? and BLOCKED is FALSE",
        groupsQuery = "select userGroups_NAME from USER_ACCOUNT_USER_GROUP where userAccounts_EMAIL = ?",
        priority = 1
)
@ApplicationScoped
public class CustomHttpAuthenticationMechanism implements
        HttpAuthenticationMechanism {

    private static final Logger LOG = Logger.getLogger(
            CustomHttpAuthenticationMechanism.class.getName());

    public static final String JWT = "JWT";

    @Inject
    private IdentityStoreHandler idStoreHandler;

    @Inject
    private XSRFTokenGenerator generator;

    @Inject
    private JWTManager jwtManager;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest req,
            HttpServletResponse rsp, HttpMessageContext hmc) throws
            AuthenticationException {

        // DEBUG Purposes
        LOG.log(Level.INFO, "Validating a request : {0} / {1}",
                new Object[]{
                    hmc.isProtected() ? "PROTECTED" : "UNPROTECTED",
                    hmc.isAuthenticationRequest() ? "AUTHENTICATION" : "NORMAL"
                });

        /**
         * Checks if the request is a login request
         */
        if (req.getParameter("username") != null
                && req.getParameter("password") != null
                // path should ends with login...
                && req.getPathInfo().endsWith("login")) {

            String username = req.getParameter("username");
            String password = req.getParameter("password");
            boolean rmbme = Boolean.valueOf(req.getParameter("rmbme"));

            try {
                // controls the credential from the IdentityStores (DB is default)
                CredentialValidationResult result = idStoreHandler.validate(
                        new UsernamePasswordCredential(
                                username, new Password(password)));

                /**
                 * Credentials of blocked users will be INVALID and access will
                 * be UNAUTHORIZED
                 */
                if (result.getStatus()
                        != CredentialValidationResult.Status.VALID) {
                    LOG.log(Level.WARNING,
                            "Authentication of {0} FAILED - Status : {1} CREDENTIAL",
                            new Object[]{username, result.getStatus().
                                        toString()});
                    return hmc.responseUnauthorized();
                }

                /**
                 * Generates an anti-XSRF token and generate a JWT with it
                 */
                String xsrf = generator.generate();
                String jwt = jwtManager.createToken(
                        result.getCallerPrincipal().getName(),
                        new ArrayList<>(result.getCallerGroups()),
                        xsrf,
                        rmbme);
                rsp.addCookie(this.createXSRFCookie(xsrf));
                rsp.addCookie(this.createJWTCookie(jwt));

                /**
                 * Confirm the access
                 */
                return hmc.notifyContainerAboutLogin(result);
            } catch (Exception ex) {
                /**
                 * Handles exceptions generated during validation from
                 * IdentityStore (hash issues...)
                 */
                LOG.log(Level.SEVERE,
                        "Authentication of " + username
                        + " FAILED : INTERNAL ERROR", ex);
                return hmc.responseUnauthorized();
            }
        }

        /**
         * Checks if the JWT is in a Cookie, gets it, verifies/controls it
         */
        if (req.getCookies() != null) {
            return Arrays.asList(req.getCookies())
                    .stream()
                    .filter(Objects::nonNull)// avoid null and empty element
                    .filter(c -> JWT.equals(c.getName()))
                    .findFirst()
                    //check if the token is revoked
                    .filter(c -> !this.jwtManager.isRevoked(c.getValue()))
                    .flatMap(c -> this.jwtManager
                            .extractClaimsSet(c.getValue()))
                    // JWT is valid and signature is verified
                    .filter(clmset -> !this.jwtManager.hasExpired(clmset))
                    .map(clmset -> {
                        try {
                            //share the anti xsrf-token with the filters as a request attribute
                            req.setAttribute("XSRF-TOKEN", clmset.getClaim(
                                    "xsrf"));

                            if (this.jwtManager.canBeRefreshed(clmset)) {
                                try {
                                    //x-xsrf-token is reused...
                                    String jwt = this.jwtManager
                                            .refreshToken(clmset);
                                    rsp.addCookie(this.createJWTCookie(jwt));
                                } catch (Exception ex) {
                                    LOG.log(Level.SEVERE,
                                            "Impossible to refresh a JWT", ex);
                                }
                            }

                            return hmc.notifyContainerAboutLogin(
                                    //get the upn or the subject (MP-JWT)
                                    Optional.
                                            ofNullable(clmset.getStringClaim(
                                                    "upn"))
                                            .filter(s -> !s.isEmpty())
                                            .orElse(clmset.getSubject()),
                                    new HashSet<>(clmset.
                                            getStringListClaim("groups")));
                        } catch (ParseException ex) {
                            LOG.log(Level.SEVERE,
                                    "A valid JWT with a verified signature cannot be parsed: for Security reasons, access will be refused !",
                                    ex);
                            return hmc.responseUnauthorized();
                        }
                    })
                    .orElseGet(() -> this.controlProtectedResource(hmc));
        }

        return this.controlProtectedResource(hmc);

    }

    /**
     * Controls and switch a request assuming it is a protected resource or not
     *
     * @param hmc the Jttp Message Context
     * @return transferts to filters or refuses the access
     */
    private AuthenticationStatus controlProtectedResource(HttpMessageContext hmc) {
        return hmc.isProtected() ? hmc.responseUnauthorized() : hmc.doNothing();
    }

    private Cookie createAuthenticationCookie(String name, String token) {
        Cookie c = new Cookie(name, token);
        c.setPath("/");
        c.setHttpOnly(false);//should be used by javascript (Angular) scripts
        c.setSecure(true);//requires to use HTTPS
        c.setMaxAge(Integer.MAX_VALUE); // converts in second
        return c;
    }

    private Cookie createXSRFCookie(String token) {
        return createAuthenticationCookie("XSRF-TOKEN", token);
    }

    private Cookie createJWTCookie(String token) {
        return createAuthenticationCookie(JWT, token);
    }

}
