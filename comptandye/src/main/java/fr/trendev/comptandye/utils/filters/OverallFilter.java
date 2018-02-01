/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
import fr.trendev.comptandye.beans.xsrf.XSRFTokenGenerator;
import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
public class OverallFilter implements Filter {

    @Inject
    ActiveSessionTracker tracker;

    @Inject
    XSRFTokenGenerator generator;

    private static final Logger LOG = Logger.getLogger(OverallFilter.class.
            getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "OverallFilter: init in progress...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;

        Principal user = req.getUserPrincipal();

        HttpSession session = req.getSession();

        LOG.log(Level.INFO, "{3} / [{1}] has requested {2} {0}",
                new Object[]{req.getRequestURL(), (user != null) ? user.
                    getName() : "an ANONYMOUS user", req.getMethod(), req.
                    getRemoteAddr()});

        /**
         * If the user is authenticated and the session is not already in the
         * tracker, adds and links it in the tracker. Usually, this can happen
         * after a redeployment (DEV purposes) or if an user is logged with the
         * basic form.
         */
        try {
            if (user != null
                    && session != null
                    && !tracker.contains(user.getName(), session)) {
                tracker.put(user.getName(), session);

                //sets the XSRF token, JSESSIONID is already pushed
                String token = generator.generate();
                session.setAttribute("XSRF-TOKEN", token);
                Cookie c = new Cookie("XSRF-TOKEN", token);
                c.setPath("/");
                c.setHttpOnly(false);//should be used by javascript (Angular) scripts
                c.setSecure(true);//requires to use HTTPS
                rsp.addCookie(c);
                LOG.log(Level.WARNING,
                        "User [{0}] authenticated but adding the XSRF-Token and the session [{1}] in the Tracker",
                        new Object[]{user.getName(),
                            session.getId()});
            }
            chain.doFilter(request, response);
        } catch (Exception ex) {
            LOG.log(Level.WARNING,
                    "The request {0} is aborted : Session already invalidated ! ",
                    req.getRequestURL());
            rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "OverallFilter: destroyed...");
    }

}
