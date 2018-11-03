/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.webfilters;

import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
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
import javax.servlet.http.HttpSession;

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

        //do not create a session if the user is not authenticated
        HttpSession session = req.getSession(false);

        String xxsrftoken = req.getHeader("X-XSRF-TOKEN");

        //allows access to no side effects methods
        if (sem.contains(req.getMethod())) {
            if (xxsrftoken != null && !xxsrftoken.isEmpty()) {
                try {
                    LOG.log(Level.INFO, "Controlling X-XSRF-TOKEN");
                    String xsrftoken = (String) session.getAttribute(
                            "XSRF-TOKEN");
                    if (xsrftoken == null
                            || xsrftoken.isEmpty()
                            || !xsrftoken.equals(xxsrftoken)) {
                        LOG.log(Level.WARNING,
                                "Unauthorized Access : XSRF-TOKEN={0} ; X-XSRF-TOKEN={1}",
                                new Object[]{xsrftoken, xxsrftoken});
                        rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    } else {
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
                String origin = req.getHeader("Origin");
                if (isOriginAllowed(origin)) {
                    LOG.log(Level.INFO,
                            "No X-XSRF-TOKEN specified in the Header but Origin is trusted !");
                    chain.doFilter(request, response);
                } else {
                    LOG.log(Level.WARNING,
                            "No X-XSRF-TOKEN specified in the Header and Origin [{0}] is not trusted !",
                            origin);
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
            }
        } else {
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

}
