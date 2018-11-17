/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

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
        dataSourceLookup = "jdbc/MySQLDataSourceComptaNdye",
        callerQuery = "select PASSWORD from USER_ACCOUNT where EMAIL=?",
        groupsQuery = "select userGroups_NAME from USER_ACCOUNT_USER_GROUP where userAccounts_EMAIL = ?",
        priority = 1
)
@ApplicationScoped
public class CustomHttpAuthenticationMechanism implements
        HttpAuthenticationMechanism {

    private static final Logger LOG = Logger.getLogger(
            CustomHttpAuthenticationMechanism.class.getName());

    @Inject
    private IdentityStoreHandler idStoreHandler;

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
                //TODO : handle error with hash
                CredentialValidationResult result = idStoreHandler.validate(
                        new UsernamePasswordCredential(
                                username, new Password(password)));

                if (result.getStatus()
                        != CredentialValidationResult.Status.VALID) {
                    LOG.log(Level.WARNING,
                            "Authentication of {0} FAILED - Status : {1} CREDENTIAL",
                            new Object[]{username, result.getStatus().
                                        toString()});
                    return hmc.responseUnauthorized();
                }

                //TODO: create session
                return hmc.notifyContainerAboutLogin(
                        result.getCallerPrincipal(),
                        result.getCallerGroups());
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

        return hmc.doNothing();
    }

}
