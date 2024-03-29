/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.controllers;

import fr.trendev.kiam.useraccount.entities.UserAccountType;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
public class AuthenticationHelper {

    /**
     * Controls if the user of the security context is in a supported group
     *
     * @param sec the security context
     * @return the user's email (principal's name) or null if context is not
     * secured or user is not in a supported group
     */
    public Optional<String> getUserEmailFromSecurityContext(
            SecurityContext sec) {
        if (sec != null
                && (sec.isUserInRole(UserAccountType.PROFESSIONAL)
                || sec.isUserInRole(UserAccountType.INDIVIDUAL)
                || sec.isUserInRole(UserAccountType.ADMINISTRATOR))) {
            return Optional.of(sec.getUserPrincipal().getName());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns the user's email
     *
     * @param sec the Security Context
     * @param email the provided user's email if not set in the Security Context
     * @param type the user type (Professional, Individual, Administrator...)
     * @return an Optional with the user's email
     */
    private Optional<String> getUserEmail(SecurityContext sec,
            String email,
            String type) {

        if (sec != null
                && sec.isUserInRole(UserAccountType.ADMINISTRATOR)) {
            return Optional.ofNullable(email);
        }

        if ((sec != null
                && sec.isUserInRole(type))) {
            return Optional.ofNullable(sec.getUserPrincipal().getName());
        }

        return Optional.empty();

    }

    /**
     * Extracts the email of professional from the SecurityContext or provides
     * the email found in the query.
     *
     * @param sec the security context
     * @param professional the owner's email provided in the query parameters
     * @return the professional's email
     */
    public String getProEmail(SecurityContext sec, String professional) {
        return this.
                getUserEmail(sec, professional, UserAccountType.PROFESSIONAL)
                .map(Function.identity())
                .orElseThrow(()
                        -> new BadRequestException(
                        "Impossible to find the professional's email from the SecurityContext or from the Query Parameters"));
    }

    public Optional<String> getJWTFromRequestHeader(
            HttpServletRequest req) {
        return Optional.ofNullable(req.getHeader("Authorization"))
                .filter(Objects::nonNull)// avoid null and empty element
                // get the token (JWT)
                .map(a -> a.substring("Bearer ".length(), a.length()));
    }

}
