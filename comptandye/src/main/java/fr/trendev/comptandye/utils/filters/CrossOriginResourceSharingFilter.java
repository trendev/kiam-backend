/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

import java.io.IOException;
import java.security.Principal;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
//@WebFilter(urlPatterns = {"/restapi/*"}, asyncSupported = true)
public class CrossOriginResourceSharingFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(
            CrossOriginResourceSharingFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");

        if (Objects.nonNull(origin) && !origin.isEmpty() && (origin.startsWith(
                "http://localhost") || origin.startsWith("https://localhost"))) {
            resp.addHeader("Access-Control-Allow-Origin", origin);
        } else {
            resp.addHeader("Access-Control-Allow-Origin",
                    "https://www.comptandye.fr:443");
        }

        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.addHeader("Access-Control-Allow-Methods",
                "OPTIONS, GET, POST, PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Headers",
                "Content-Type, Accept");
        Principal user = req.getUserPrincipal();

        LOG.log(Level.INFO, "{3} / [{1}] has requested {2} {0} and we add CORS",
                new Object[]{req.getRequestURL(), (user != null) ? user.
                    getName() : "an ANONYMOUS user", req.getMethod(), req.
                    getRemoteAddr()});

        chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO,
                "CrossOriginResourceSharingFilter : init in progress...");
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "CrossOriginResourceSharingFilter : destroyed...");
    }

}
