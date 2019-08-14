/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.filters;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author jsie
 */
@Provider
@RequestScoped
public class UserAccountFilter implements ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(UserAccountFilter.class.getName());

    private final String className = UserAccountFilter.class.getSimpleName();

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        if ("user-account/create-professional".equals(requestContext.getUriInfo().getPath())
                && responseContext.getStatusInfo() == Response.Status.BAD_REQUEST) {

            LOG.log(Level.WARNING, "{0} : overriding Response due to Professional Creation failure", className);

            responseContext.setEntity(
                    Json.createObjectBuilder()
                            .add("error", "Bad JSON format")
                            .build());
        }
    }

}
