/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import static fr.trendev.comptandye.security.controllers.JWTManager.VALID_PERIOD;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
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

    private static final String JWT = "JWT";

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

        LOG.log(Level.INFO, "Validating a request : {0} / {1}", new Object[]{
            hmc.isProtected() ? "PROTECTED" : "UNPROTECTED",
            hmc.isAuthenticationRequest() ? "AUTHENTICATION" : "NORMAL"});

        if (req.getParameter("username") != null
                && req.getParameter("password") != null) {

            String username = req.getParameter("username");
            String password = req.getParameter("password");

            try {
                CredentialValidationResult result = idStoreHandler.validate(
                        new UsernamePasswordCredential(
                                username, new Password(password)));

                /**
                 * Credentials of a blocked user will be INVALID
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
                String jwt = jwtManager.generateToken(
                        result.getCallerPrincipal().getName(),
                        new ArrayList<>(result.getCallerGroups()),
                        xsrf);
                rsp.addCookie(this.createXSRFCookie(xsrf));
                rsp.addCookie(this.createJWTCookie(jwt));

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

        if (req.getCookies() != null) {
            Arrays.asList(req.getCookies())
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(c -> JWT.equals(c.getName()))
                    .findFirst()
                    .ifPresent(c -> {
                        String token = c.getValue();
                        //TODO : control the token 
                        LOG.info("JWT = " + token);
                    });
        }

        return hmc.isProtected() ? hmc.responseUnauthorized() : hmc.doNothing();
    }

    private Cookie createAuthenticationCookie(String name, String token) {
        Cookie c = new Cookie(name, token);
        c.setPath("/");
        c.setHttpOnly(false);//should be used by javascript (Angular) scripts
        c.setSecure(true);//requires to use HTTPS
        c.setMaxAge(60 * VALID_PERIOD); // converts in second
        return c;
    }

    private Cookie createXSRFCookie(String token) {
        return createAuthenticationCookie("XSRF-TOKEN", token);
    }

    private Cookie createJWTCookie(String token) {
        return createAuthenticationCookie(JWT, token);
    }

}
