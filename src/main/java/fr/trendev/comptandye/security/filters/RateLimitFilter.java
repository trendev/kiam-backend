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
import javax.ws.rs.core.Response;
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
        if (arc.control(req.getRemoteAddr(),
                cr.getUriInfo().getPath()).isPresent()) {
            LOG.log(Level.WARNING, "{1} : RemoteAddr {0} >>> TOO_MANY_REQUESTS",
                new Object[]{
                    req.getRemoteAddr(),
                    className});
            cr.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS).build());
        }
    }
    
}
