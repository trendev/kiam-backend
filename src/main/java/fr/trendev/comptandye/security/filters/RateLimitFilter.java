/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.security.controllers.ratelimit.RateLimitController;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
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

        Optional<Boolean> result = arc.control(req.getRemoteAddr(),
                cr.getUriInfo().getPath());

        if (result.isPresent()) {
            LOG.log(Level.WARNING, "{1} : RemoteAddr {0} >>> TOO_MANY_REQUESTS {2}/{2}",
                    new Object[]{
                        req.getRemoteAddr(),
                        className,
                        result.get()});
            cr.abortWith(Response
                    .status(Response.Status.TOO_MANY_REQUESTS)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(Json.createObjectBuilder()
                            .add("error", "TOO_MANY_REQUESTS")
                            .build())
                    .build());
        }
    }

}
