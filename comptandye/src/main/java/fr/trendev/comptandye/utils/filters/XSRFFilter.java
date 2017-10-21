/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.filters;

import fr.trendev.comptandye.utils.exceptions.ExceptionHelper;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author jsie
 */
public class XSRFFilter implements Filter {

    /**
     * Side Effect Methods
     */
    private List<String> sem;

    private static final Logger LOG = Logger.getLogger(XSRFFilter.class.
            getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.log(Level.INFO, "XSRFFilter: init in progress...");
        this.sem = Arrays.asList("POST", "PUT", "DELETE");
    }

    private boolean isTrustedOrigin(HttpServletRequest req) {
        String origin = req.getHeader("Origin");
        return Objects.nonNull(origin) && !origin.isEmpty() && (origin.
                startsWith(
                        "http://localhost") || origin.startsWith(
                        "https://localhost"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse rsp = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        String xxsfrtoken = req.getHeader("X-XSRF-TOKEN");

        if (sem.contains(req.getMethod())) {
            if (xxsfrtoken != null && !xxsfrtoken.isEmpty()) {
                try {
                    LOG.log(Level.INFO, "Checking X-XSFR-TOKEN");
                    String xsfrtoken = (String) session.getAttribute(
                            "XSRF-TOKEN");
                    if (xsfrtoken == null
                            || xsfrtoken.isEmpty()
                            || !xsfrtoken.equals(xxsfrtoken)) {
                        LOG.log(Level.WARNING,
                                "Unauthorized Access : XSFR-TOKEN={0} ; X-XSFR-TOKEN={1}",
                                new Object[]{xsfrtoken, xxsfrtoken});
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
                if (this.isTrustedOrigin(req)) {
                    chain.doFilter(request, response);
                } else {
                    LOG.log(Level.WARNING,
                            "No X-XSFR-TOKEN specified in the Header and Origin is not trusted !");
                    rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
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
