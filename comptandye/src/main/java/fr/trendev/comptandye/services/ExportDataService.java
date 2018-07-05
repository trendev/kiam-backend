/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.utils.AuthenticationSecurityUtils;
import fr.trendev.comptandye.utils.exceptions.ExceptionHandler;
import fr.trendev.comptandye.utils.export.JsonProfessionalExporter;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Data/Export")
@RolesAllowed({"Administrator", "Professional"})
public class ExportDataService {

    private final Logger LOG = Logger.getLogger(ExportDataService.class.
            getName());

    @Inject
    private JsonProfessionalExporter jsonProExport;

    @Inject
    private AuthenticationSecurityUtils authenticationSecurityUtils;

    @Inject
    protected ExceptionHandler exceptionHandler;

    @GET
    @Path("json")
    @Produces(MediaType.APPLICATION_JSON)
    public void exportAsJson(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("professional") String professional) {

        String email = authenticationSecurityUtils.
                getProEmail(sec, professional);

        LOG.log(Level.INFO, "Exporting data of Professional " + email);

        CompletableFuture
                .supplyAsync(() -> jsonProExport.export(email))
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));
    }

    @GET
    @Path("jsonfile")
    @Produces({MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON,})
    public void exportAsJsonFile(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("professional") String professional) {

        String email = authenticationSecurityUtils.
                getProEmail(sec, professional);

        LOG.log(Level.INFO, "Exporting data of Professional " + email
                + " into a json file");

        CompletableFuture
                .supplyAsync(() -> jsonProExport.export(email, true))
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));
    }

}
