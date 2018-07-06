/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.utils.AuthenticationSecurityUtils;
import fr.trendev.comptandye.utils.backups.JsonProfessionalExporter;
import fr.trendev.comptandye.utils.backups.JsonProfessionalImporter;
import fr.trendev.comptandye.utils.exceptions.ExceptionHandler;
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
@Path("Data")
@RolesAllowed({"Administrator", "Professional"})
public class DataService {
    
    private final Logger LOG = Logger.getLogger(DataService.class.
            getName());
    
    @Inject
    private JsonProfessionalExporter jsonProExport;
    
    @Inject
    private JsonProfessionalImporter jsonProImport;
    
    @Inject
    private AuthenticationSecurityUtils authenticationSecurityUtils;
    
    @Inject
    protected ExceptionHandler exceptionHandler;
    
    @GET
    @Path("export/json")
    @Produces(MediaType.APPLICATION_JSON)
    public void exportAsJson(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @QueryParam("f") boolean asFile,
            @QueryParam("professional") String professional) {
        
        String email = authenticationSecurityUtils.
                getProEmail(sec, professional);
        
        LOG.log(Level.INFO, "Exporting data of Professional " + email
                + (asFile ? " into a json file" : ""));
        
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
     * @param sjpb serialized JsonProfessionalBackup
     */
    @POST
    @Path("control/json")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public void controlJson(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            InputStream is) {
        
        LOG.log(Level.WARNING, "Receiving a backup...");
        
        authenticationSecurityUtils.getProfessionalEmailFromSecurityContext(sec)
                .ifPresent(email -> LOG.log(Level.INFO, email
                        + " is controlling a backup..."));
        
        CompletableFuture
                .supplyAsync(() -> jsonProImport.builResponse(is))
                .thenApply(result -> ar.resume(result))
                .exceptionally(e -> ar.resume(exceptionHandler.handle(e)));
        
    }
    
}
