/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

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
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
@WebFilter(urlPatterns = {"/*"}, asyncSupported = true)
public class CrossOriginResourceSharingFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(
            CrossOriginResourceSharingFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        Principal user = req.getUserPrincipal();

        LOG.log(Level.INFO, "[{1}] has requested {0} and we're adding CORS",
                new Object[]{req.getRequestURL(), (user != null) ? user.
                    getName() : "ANONYMOUS user"});

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods",
                "OPTIONS, GET, POST, PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Headers",
                "Content-Type, api_key, Authorization");

        chain.doFilter(request, response);

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO,
                "CrossOriginResourceSharingFilter : init(FilterConfig filterConfig)");
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "CrossOriginResourceSharingFilter : destroy()");
    }

}
