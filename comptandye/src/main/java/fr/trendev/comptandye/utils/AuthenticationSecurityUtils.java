/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.services.AuthenticationService;
import java.util.Optional;
import java.util.function.Function;
import javax.ejb.Stateless;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
public class AuthenticationSecurityUtils {

    public Optional<String> getProfessionalEmailFromSecurityContext(
            SecurityContext sec) {
        return Optional.ofNullable(sec.isSecure() && (sec.isUserInRole(
                UserAccountType.PROFESSIONAL) || sec.isUserInRole(
                        UserAccountType.INDIVIDUAL) || sec.isUserInRole(
                        UserAccountType.ADMINISTRATOR)) ? sec.getUserPrincipal().
                                getName() : null);
    }

    public boolean isBlockedUser(SecurityContext sec,
            AuthenticationService authenticationService) {
        return this.getProfessionalEmailFromSecurityContext(sec).
                map(u -> false).orElse(true);
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
        return Optional.ofNullable(sec.isSecure() &&
                sec.isUserInRole(UserAccountType.PROFESSIONAL) ? sec.getUserPrincipal().
                getName() : professional).map(Function.identity()).
                orElseThrow(() ->
                new BadRequestException("Impossible to find the professional's email from the SecurityContext or from the Query Parameters"));
    }

}
