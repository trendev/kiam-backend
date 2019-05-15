/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.backup.boundaries;

import fr.trendev.comptandye.backup.controllers.JsonProfessionalExporter;
import fr.trendev.comptandye.backup.controllers.JsonProfessionalImporter;
import fr.trendev.comptandye.exceptions.ExceptionHandler;
import fr.trendev.comptandye.security.controllers.AuthenticationHelper;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("Backup")
@RolesAllowed({"Administrator", "Professional"})
public class BackupService {

    private final Logger LOG = Logger.getLogger(BackupService.class.
            getName());

    @Inject
    private JsonProfessionalExporter jsonProExport;

    @Inject
    private JsonProfessionalImporter jsonProImport;

    @Inject
    private AuthenticationHelper authenticationHelper;

    @Inject
    protected ExceptionHandler exceptionHandler;

    @GET
    @Path("export/json")
    @Produces(MediaType.APPLICATION_JSON)
    public void exportAsJson(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("f") boolean asFile,
            @QueryParam("professional") String professional) {

        String email = authenticationHelper.
                getProEmail(sec, professional);

        LOG.log(Level.INFO, "Exporting data of Professional {0}{1}",
                new Object[]{email, asFile ? " into a json file" : ""});

        CompletableFuture
                .supplyAsync(() -> jsonProExport.export(email, asFile))
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));
    }

    /**
     * Controls if the JsonProfessionalBackup provided as is valid or not.
     *
     * @param ar the Asynchronous Response
     * @param sec the security context
     * @param is the stream including the Professional object
     */
    @POST
    @Path("control/json")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public void controlJson(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            InputStream is) {

        LOG.log(Level.INFO, "Receiving a backup...");

        authenticationHelper.getUserEmailFromSecurityContext(sec)
                .ifPresent(email -> LOG.log(Level.INFO,
                        "{0} is controlling a backup...", email));

        CompletableFuture
                .supplyAsync(() -> jsonProImport.control(is))
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));

    }

}
