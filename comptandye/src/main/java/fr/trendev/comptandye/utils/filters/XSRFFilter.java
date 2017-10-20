/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

import fr.trendev.comptandye.beans.ActiveSessionTracker;
import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
public class XSRFFilter implements Filter {

    @Inject
    ActiveSessionTracker tracker;

    private List<String> authorizedMethods;

    private static final Logger LOG = Logger.getLogger(XSRFFilter.class.
            getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "XSRFFilter: init in progress...");
        this.authorizedMethods = Arrays.asList("POST", "PUT", "DELETE");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;

        Principal user = req.getUserPrincipal();

        HttpSession session = req.getSession();

        if (authorizedMethods.contains(req.getMethod())) {
            try {
                String xsfrtoken = (String) session.getAttribute("XSRF-TOKEN");
                String xxsfrtoken = req.getHeader("X-XSRF-TOKEN");
                if (xsfrtoken == null || xxsfrtoken == null
                        || !xsfrtoken.equals(xxsfrtoken)) {
                    LOG.log(Level.WARNING, "XSFR-TOKEN=" + xsfrtoken
                            + " ; X-XSFR-TOKEN=" + xxsfrtoken);
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                } else {
                    chain.doFilter(request, response);
                }
            } catch (Exception ex) {
                String errmsg = ExceptionHelper.handleException(ex,
                        "Error in Filter " + XSRFFilter.class.getSimpleName());
                LOG.log(Level.WARNING, errmsg);
                rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
        LOG.log(Level.INFO, "XSRFFilter: destroyed...");
    }

}
