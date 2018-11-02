/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.security;

import fr.trendev.comptandye.utils.UserAccountType;
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

    /**
     * Controls if the context is secured (HTTPS) and if the user of the
     * security context is in a support group
     *
     * @param sec the security context
     * @return the user's email (principal's name) or null if context is not
     * secured or user is not in a supported group
     */
    public Optional<String> getProfessionalEmailFromSecurityContext(
            SecurityContext sec) {
        return Optional.ofNullable(
                sec.isSecure()
                && (sec.isUserInRole(UserAccountType.PROFESSIONAL)
                || sec.isUserInRole(UserAccountType.INDIVIDUAL)
                || sec.isUserInRole(UserAccountType.ADMINISTRATOR))
                ? sec.getUserPrincipal().getName() : null);
    }

    /**
     * Controls if an user is blocked or not. If a user has been blocked (using
     * the admin interface), it should have been set as blocked and removed from
     * its group:getProfessionalEmailFromSecurityContext() should return null
     * and this method should return false in this case.
     *
     * @param sec
     * @return
     */
    public boolean isBlockedUser(SecurityContext sec) {
        return getProfessionalEmailFromSecurityContext(sec).
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
        return Optional.ofNullable(sec.isSecure() && sec.isUserInRole(
                UserAccountType.PROFESSIONAL) ? sec.getUserPrincipal().
                                getName() : professional).map(Function.
                        identity()).
                orElseThrow(() ->
                        new BadRequestException(
                                "Impossible to find the professional's email from the SecurityContext or from the Query Parameters"));
    }

}
