/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
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

        if (user != null && req.getSession() != null
                && !tracker.contains(user.getName(), req.getSession())) {
            tracker.put(user.getName(), req.getSession());
            int count = tracker.count(user.getName());
            LOG.log(Level.INFO,
                    "New session {0} added to ActiveSessionTracker. [{1}] has now {2} active session{3}",
                    new Object[]{req.getSession().getId(),
                        user.getName(), count, count > 1 ? "s" : ""});
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "OverallFilter: destroyed...");
    }

}
