/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.webfilters;

import fr.trendev.comptandye.security.controllers.XSRFTokenGenerator;
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
    XSRFTokenGenerator generator;

    private static final Logger LOG = Logger.getLogger(OverallFilter.class.
            getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "OverallFilter: init in progress...");
    }

    /**
     * Filters all HTTP requests and logs the request. If the user is
     * authenticated and the session is valid but not in the
     * ActiveSessionTracker, the session is added in the tracker, an XSRF-TOKEN
     * is generated and added in the session. A timestamp is also added in the
     * session in order to avoid using the lastAccessedTime of the HttpSession.
     * Session is marked with a specific attribute RQT_TIMESTAMP and the current
     * timestamp.
     *
     * @param request the request to filter
     * @param response the expected response
     * @param chain the filters chain
     * @throws IOException an IOException
     * @throws ServletException a Servlet Exception
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;

        Principal user = req.getUserPrincipal();

        //do not create a session if the user is not authenticated
        HttpSession session = req.getSession(false);

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
                    && session != null) {

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
            /**
             * Adds a timestamp in the current session in order to store the
             * last access time and not the last access time of the previous
             * request.
             *
             * @see
             */
            if (user != null && session != null) {
                session.
                        setAttribute("RQT_TIMESTAMP", System.currentTimeMillis());
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
