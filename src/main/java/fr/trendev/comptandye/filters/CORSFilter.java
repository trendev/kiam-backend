/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author jsie
 */
//@WebFilter(urlPatterns = {"/restapi/*"}, asyncSupported = true)
public class CORSFilter implements Filter {
    
    private final String className = CORSFilter.class.getSimpleName();

    private static final Logger LOG = Logger.getLogger(CORSFilter.class.
            getName());

    /**
     *
     * Checks if the Cross-Origin Request is localhost. Only cross-origin
     * requests coming from localhost are supported. Supports for development
     * purposes only. This is not a security issue because it requires a local
     * running web server (will use the local logged sessions).
     *
     * @param request the http request
     * @param response the http respons
     * @param chain the filter chain
     * @throws IOException thrown if an exception occurs during the doFilter
     * @throws ServletException thrown if an exception occurs during the
     * doFilter
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {

        HttpServletResponse resp = (HttpServletResponse) response;

        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Credentials", "true");
        resp.addHeader("Access-Control-Allow-Methods",
                "OPTIONS, GET, POST, PUT, DELETE");
        resp.addHeader("Access-Control-Allow-Headers",
                "origin, content-type, accept, authorization");
        resp.addHeader("Access-Control-Expose-Headers", "jwt");

        LOG.log(Level.INFO, "{0} : Adding CORS", className);

        chain.doFilter(request, response);

    }

    /**
     * Logs a message when the filter starts
     *
     * @param filterConfig the filter configuration
     * @throws ServletException if an error occurs during the start-up
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "{0} : init in progress...", className);
    }

    /**
     * Logs a message when the filter is destroyed
     */
    @Override
    public void destroy() {
        LOG.log(Level.INFO, "{0} : destroyed...", className);
    }

}
