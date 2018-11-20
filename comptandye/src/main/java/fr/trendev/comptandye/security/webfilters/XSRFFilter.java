/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.webfilters;

import fr.trendev.comptandye.exceptions.ExceptionHelper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
public class XSRFFilter extends ApiFilter {

    /**
     * Side Effect Methods : PUT,POST,DELETE. Other side effects method are not
     * supported on the REST API Services
     */
    private List<String> sem;

    private static final Logger LOG = Logger.getLogger(XSRFFilter.class.
            getName());

    /**
     * Logs a message when the filter starts
     *
     * @param filterConfig the filter configuration
     * @throws ServletException if an error occurs during the start-up
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "XSRFFilter: init in progress...");
        this.sem = Arrays.asList("POST", "PUT", "DELETE");
    }

    /**
     * Filters the REST API requests and protects them against XSRF attacks.
     * Only supports localhost cross-origin requests.
     *
     * @param request the request to check
     * @param response the expected response or Unauthorized response if the
     * request is not compliant (block XSRF attacks).
     * @param chain the filter chain
     * @throws IOException if an error occurs during the filtering
     * @throws ServletException if an error occurs during the filtering
     * @see CORSFilter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;

        String xxsrftoken = req.getHeader("X-XSRF-TOKEN");

        /**
         * First, controls mutating requests (with side effects)
         */
        if (sem.contains(req.getMethod())) {
            if (xxsrftoken != null && !xxsrftoken.isEmpty()) {
                /**
                 * Checks the XSRF token and compares with the provided one (as
                 * an attribute dynamically added in the request by the
                 * AuthenticationMechanism)
                 */
                try {
                    LOG.log(Level.INFO, "Controlling X-XSRF-TOKEN");
                    String xsrftoken = this.xsrfAttribute(req);
                    if (xsrftoken == null
                            || xsrftoken.isEmpty()
                            || !xsrftoken.equals(xxsrftoken)) {
                        /**
                         * Incorrect or no X-XSRF-TOKEN token
                         */
                        LOG.log(Level.WARNING,
                                "Unauthorized Access : XSRF-TOKEN={0} ; X-XSRF-TOKEN={1}",
                                new Object[]{xsrftoken, xxsrftoken});
                        rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
                        /**
                         * Valid token, request accepted
                         */
                        chain.doFilter(request, response);
                    }
                } catch (Exception ex) {
                    String errmsg = ExceptionHelper.handleException(ex,
                            "Error in Filter " + XSRFFilter.class.
                                    getSimpleName());
                    LOG.log(Level.WARNING, errmsg);
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            } else {
                /**
                 * No X-XSRF-TOKEN provided : non mutating request or absolute
                 * URL
                 */
                String origin = req.getHeader("Origin");
                if (isOriginAllowed(origin)) {
                    /**
                     * Checks the origin header of the absolute URL
                     */
                    LOG.log(Level.INFO,
                            "No X-XSRF-TOKEN specified in the Header but Origin is trusted !");
                    chain.doFilter(request, response);
                } else {
                    /**
                     * No token and not allowed origin : request is not
                     * authorized
                     */
                    LOG.log(Level.WARNING,
                            "No X-XSRF-TOKEN specified in the Header and Origin [{0}] is not trusted !",
                            origin);
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } else {
            /**
             * Accept non mutating requests (GET, HEAD or OPTION
             */

            chain.doFilter(request, response);
        }
    }

    /**
     * Logs a message when the filter is destroyed
     */
    @Override
    public void destroy() {
        LOG.log(Level.INFO, "XSRFFilter: destroyed...");
    }

    private String xsrfAttribute(HttpServletRequest req) {
        Object attrib = req.getAttribute("XSRF-TOKEN");
        if (attrib != null) {
            return String.valueOf(attrib);
        }
        return null;
    }

}
