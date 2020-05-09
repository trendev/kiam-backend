/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import com.nimbusds.jwt.JWTClaimsSet;
import fr.trendev.kiam.security.controllers.jwt.JWTManager;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
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
        dataSourceLookup = "java:global/kiam/MySQLDataSource",
        callerQuery = "select PASSWORD from USER_ACCOUNT where EMAIL=? and BLOCKED is FALSE",
        groupsQuery = "select userGroups_NAME from USER_ACCOUNT_USER_GROUP where userAccounts_EMAIL = ?",
        priority = 1
)
@ApplicationScoped
public class DefaultHttpAuthenticationMechanism implements
        HttpAuthenticationMechanism {

    private static final Logger LOG = Logger.getLogger(
            DefaultHttpAuthenticationMechanism.class.getName());

    public static final String JWT = "JWT";

    @Inject
    private IdentityStoreHandler idStoreHandler;

    @Inject
    private JWTManager jwtManager;

    @Inject
    private AuthenticationHelper authHelper;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest req,
            HttpServletResponse rsp, HttpMessageContext hmc) throws
            AuthenticationException {

        String uri = req.getRequestURI();

        if (!"/health/live".equals(uri) && !"/health/ready".equals(uri)) { // prevent healthcheck logs
            LOG.log(Level.INFO, "{3} - {2} : {0} {1}",
                    new Object[]{
                        req.getMethod(),
                        req.getRequestURL(),
                        hmc.isProtected() ? "PROTECTED" : "UNPROTECTED",
                        hmc.isAuthenticationRequest() ? "AUTHENTICATION" : "NORMAL"
                    });
        }

        /**
         * control the authorization token first : prevent to login
         * authenticated users
         */
        Optional<AuthenticationStatus> as = this.controlHeaders(req, rsp, hmc);
        if (as.isPresent()) {
            return as.get();
        }

        /**
         * if authorization token is not present or not valid, control if the
         * request is a login request
         */
        if (hmc.isAuthenticationRequest() && hmc.isProtected()
                && req.getParameter("username") != null
                && req.getParameter("password") != null
                && req.getMethod().equals("GET")
                // path should ends with login...
                && req.getPathInfo().endsWith("login")) {

            String username = req.getParameter("username").trim();
            String password = req.getParameter("password").trim();
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
                            "Authentication of [{0}] / [{2}] FAILED - Status : {1} CREDENTIAL",
                            new Object[]{
                                username,
                                result.getStatus().toString(),
                                password
                            });
                    return hmc.responseUnauthorized();
                }

                String jwt = jwtManager.createToken(
                        result.getCallerPrincipal().getName(),
                        new ArrayList<>(result.getCallerGroups()),
                        rmbme);

                rsp.addHeader(JWT, jwt);

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

    private Optional<AuthenticationStatus> controlHeaders(
            HttpServletRequest req,
            HttpServletResponse rsp,
            HttpMessageContext hmc) {

        return this.authHelper.getJWTFromRequestHeader(req)
                .filter(jwt -> !this.jwtManager.isRevoked(jwt))
                .flatMap(jwt -> this.jwtManager.extractLegalClaimsSet(jwt))
                // JWT is valid
                // signature is verified
                // JWT is decoded 
                .map(d -> {
                    try {
                        JWTClaimsSet clmset = d.getClaimsSet();
                        if (this.jwtManager.canBeRefreshed(d.getClaimsSet())
                                // prevent refreshing a JWT token and block LOGOUT
                                && !req.getPathInfo().endsWith("/logout")) {
                            try {
                                String jwt = this.jwtManager.refreshToken(d);
                                rsp.addHeader(JWT, jwt);
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
                });

    }

}
