/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.webfilters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CORSFilter extends ApiFilter {

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

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");

        if (isOriginAllowed(origin)) {
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

        LOG.log(Level.INFO, "- Adding CORS -");

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
        LOG.log(Level.INFO,
                "CORSFilter : init in progress...");
    }

    /**
     * Logs a message when the filter is destroyed
     */
    @Override
    public void destroy() {
        LOG.log(Level.INFO, "CORSFilter : destroyed...");
    }

}
