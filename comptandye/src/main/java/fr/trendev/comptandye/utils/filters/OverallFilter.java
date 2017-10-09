/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author jsie
 */
public class OverallFilter implements Filter {

    @Inject
    ActiveSessionTracker tracker;

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

        Principal user = req.getUserPrincipal();

        LOG.log(Level.INFO, "{3} / [{1}] has requested {2} {0}",
                new Object[]{req.getRequestURL(), (user != null) ? user.
                    getName() : "an ANONYMOUS user", req.getMethod(), req.
                    getRemoteAddr()});

        if (user != null && req.getSession() != null) {
            LOG.log(Level.INFO, "New session {0} added to ActiveSessionTracker",
                    req.getSession().getId());
            LOG.log(Level.INFO, "creation time : {0}", new Date(
                    req.getSession().
                            getCreationTime()));
            LOG.log(Level.INFO,
                    "last access time : {0} and will be inactive in {1} seconds",
                    new Object[]{new Date(req.
                                getSession().
                                getLastAccessedTime()),
                        req.getSession().getMaxInactiveInterval()});

            for (Enumeration<String> headers = req.getHeaderNames(); headers.
                    hasMoreElements();) {
                String header = headers.nextElement();
                LOG.log(Level.INFO, "### {0} : {1}", new Object[]{header,
                    req.getHeader(
                    header)});
            }

        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "OverallFilter: destroyed...");
    }

}
