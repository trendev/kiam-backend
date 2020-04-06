/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.security.boundaries;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.kiam.security.controllers.jwt.JWTManager;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("JWTManager")
@RolesAllowed({"Administrator"})
public class JWTManagerService {

    @Inject
    private JWTManager jwtManager;

    @Inject
    private ObjectMapper om;

    @GET
    @Path("whitemap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJWTWhiteMap() throws JsonProcessingException {
        return Response.ok(this.jwtManager.getJWTWhiteMapEntries())
                .build();
    }

    @GET
    @Path("revokedset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJWTRevokedSet() throws JsonProcessingException {
        return Response.ok(this.jwtManager.getJWTRevokedSet().getSet())
                .build();
    }

    @GET
    @Path("legal-tokens")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLegalTokens() throws JsonProcessingException {
        return Response.ok(this.jwtManager.getLegalTokens())
                .build();
    }
    
    @GET
    @Path("refreshed-tokens")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRefreshedTokens() throws JsonProcessingException {
        return Response.ok(this.jwtManager.getRefreshedTokensMap())
                .build();
    }

}
