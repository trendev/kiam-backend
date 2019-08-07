/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.filters;

import fr.trendev.comptandye.security.controllers.ratelimit.RateLimitController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author jsie
 */
@PreMatching
@Provider
@RequestScoped
public class RateLimitFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(RateLimitFilter.class.getName());

    private final String className = RateLimitFilter.class.getSimpleName();

    @Context
    private HttpServletRequest req;

    @Inject
    private RateLimitController arc;

    @Override
    public void filter(ContainerRequestContext cr) {
        LOG.log(Level.INFO, ">>>{4} : {1} {2} | content-length = {3} | RemoteAddr = {0} >>>",
                new Object[]{
                    req.getRemoteAddr(),
                    req.getMethod(),
                    req.getRequestURL(),
                    cr.getLength(),
                    className});
        
       arc.control(cr.getUriInfo().getPath());
    }

}
