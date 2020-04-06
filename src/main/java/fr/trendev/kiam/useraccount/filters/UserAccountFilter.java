/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.useraccount.filters;

import fr.trendev.kiam.useraccount.entities.NewProfessionalCreated;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
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
@ApplicationScoped
public class UserAccountFilter implements ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(UserAccountFilter.class.getName());

    private final String className = UserAccountFilter.class.getSimpleName();

    @Inject
    @NewProfessionalCreated
    private Event<JsonObject> newProfessionalCreatedEvent;

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {

        if ("POST".equals(requestContext.getMethod())
                && "user-account/create-professional".equals(requestContext.getUriInfo().getPath())) {

            if (responseContext.getStatusInfo() == Response.Status.BAD_REQUEST) {
                LOG.log(Level.WARNING, "{0} : overriding Response due to Professional creation failure", className);

                responseContext.setEntity(
                        Json.createObjectBuilder()
                                .add("error", "BAD_REQUEST")
                                .build());
            }

            if (responseContext.getStatusInfo() == Response.Status.OK) {
                JsonObject entity = (JsonObject) responseContext.getEntity();

                newProfessionalCreatedEvent.fireAsync(entity);

                String email = entity.getString("email");
                responseContext.setEntity(
                        Json.createObjectBuilder()
                                .add("success", "Professional " + email + " created")
                                .build());
            }
        }
    }

}
