/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TraceRequestFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(TraceRequestFilter.class.
            getName());

    private final String className = TraceRequestFilter.class.getSimpleName();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "{0} : initialized", className);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        Principal user = req.getUserPrincipal();

        String realIP = req.getHeader("X-Real-IP");

        String uri = req.getRequestURI();

        if (!"/health/live".equals(uri) && !"/health/ready".equals(uri)) { // prevent healthcheck logs
            LOG.log(Level.INFO, "{4} : Request from [{1}] | {2} {0} | RemoteAddr = {3} | X-Real-IP = {5}",
                    new Object[]{
                        req.getRequestURL(),
                        (user != null) ? user.getName() : "an ANONYMOUS user",
                        req.getMethod().toUpperCase(),
                        req.getRemoteAddr(),
                        className,
                        realIP});

        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "{0} : destroyed", className);
    }

}
