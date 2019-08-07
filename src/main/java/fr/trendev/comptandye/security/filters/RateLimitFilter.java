/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.filters;

import fr.trendev.comptandye.security.controllers.ratelimit.RateLimitController;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import net.minidev.json.JSONObject;

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

        Optional<Map<String, List<Date>>> result = arc.control(req.getRemoteAddr(),
                cr.getUriInfo().getPath());

        if (result.isPresent()) {

            String path = cr.getUriInfo().getPath();
            List<Date> lastAccessList = result.get().get(path);
            int limit = lastAccessList.size();

            LOG.log(Level.WARNING, "{1} : RemoteAddr {0} | accessing PATH {3} >>> TOO_MANY_REQUESTS {2}/{2}",
                    new Object[]{
                        req.getRemoteAddr(),
                        className,
                        limit, path});
            cr.abortWith(this.buildTooManyRequestsResponse(path,
                    limit,
                    lastAccessList));
        }

    }

    private Response buildTooManyRequestsResponse(String path,
            int limit,
            List<Date> lastAccessList) {

        JsonObject jo = Json.createObjectBuilder()
                .add("error", "TOO_MANY_REQUESTS")
                .add("path", path)
                .add("limit", limit)
                .add("last_access_list", Json.createArrayBuilder(
                        lastAccessList.stream().map(d -> {
                            return d.getTime();
                        }).collect(Collectors.toList())).build())
                .build();

        return Response
                .status(Response.Status.TOO_MANY_REQUESTS)
                .type(MediaType.APPLICATION_JSON)
                .entity(jo)
                .build();
    }

}
