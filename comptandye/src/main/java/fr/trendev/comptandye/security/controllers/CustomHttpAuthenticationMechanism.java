/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
@ApplicationScoped
public class CustomHttpAuthenticationMechanism implements
        HttpAuthenticationMechanism {

    private static final Logger LOG = Logger.getLogger(
            CustomHttpAuthenticationMechanism.class.getName());

    @Inject
    private IdentityStoreHandler idStoreHandler;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest hsr,
            HttpServletResponse hsr1, HttpMessageContext hmc) throws
            AuthenticationException {

        if (hsr.getParameter("username") != null
                && hsr.getParameter("password") != null) {

            String username = hsr.getParameter("username");
            String password = hsr.getParameter("password");

            //TODO : handle error with hash
            CredentialValidationResult result = idStoreHandler.validate(
                    new UsernamePasswordCredential(
                            username, new Password(password)));

            if (result.getStatus() != CredentialValidationResult.Status.VALID) {
                LOG.log(Level.WARNING,
                        "Authentication of {0} FAILED - Status : {1}",
                        new Object[]{username, result.getStatus().toString()});
                return hmc.responseUnauthorized();
            }

            //TODO: create session
            return hmc.notifyContainerAboutLogin(
                    result.getCallerPrincipal(),
                    result.getCallerGroups());
        }

        return hmc.doNothing();
    }

}
