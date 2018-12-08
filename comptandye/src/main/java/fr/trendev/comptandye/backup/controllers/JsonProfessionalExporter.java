/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.backup.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.backup.entities.HashingMechanism;
import fr.trendev.comptandye.backup.entities.JsonProfessionalBackup;
import fr.trendev.comptandye.objectmapper.backupcfg.ProfessionalBackup;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
public class JsonProfessionalExporter {

    private final Logger LOG = Logger.getLogger(JsonProfessionalExporter.class.
            getName());

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    @ProfessionalBackup
    private ObjectMapper om;

    @Inject
    private HashingMechanism encryptionUtils;

    /**
     * Stringify a professional into a JsonProfessionalBackup.
     *
     * @param pro the professional to export
     * @return the stringified JsonProfessionalBackup
     */
    protected String stringify(Professional pro) {

        try {
            String json = om.writeValueAsString(pro);
            LOG.log(Level.INFO, "Graph of " + pro.getEmail()
                    + " : successfully FLATTENED");

            JsonProfessionalBackup jpb = new JsonProfessionalBackup(pro);
            jpb.initChecksum(encryptionUtils, json);
            return om.writerWithDefaultPrettyPrinter().
                    writeValueAsString(jpb);

        } catch (JsonProcessingException ex) {
            throw new WebApplicationException(
                    "Error building the Response exporting data of Professional "
                    + pro.getEmail(), ex);
        }
    }

    /**
     * Builds a Response from the stringified professional
     *
     * @param pro the professsional to export
     * @param asFile indicate if the data will be export in a file
     * @return the flattened professional's graph or 404 Not Found
     */
    protected Response builResponse(Professional pro, boolean asFile) {
        if (!asFile) {
            return Response.ok(stringify(pro)).build();
        } else {
            Response.ResponseBuilder response = Response.ok(
                    stringify(pro));
            String date = new SimpleDateFormat("yyyyMMdd-HHmm")
                    .format(new Date());
            response.header("Content-Disposition",
                    "attachment; filename=\"export_" + date
                    + "_"
                    + pro.
                            getUuid()
                    + ".json\"");
            return response.build();
        }
    }

    /**
     * Export the professional's data in json
     *
     * @param email the professional's email
     * @return the flattened professional's graph or 404 Not Found
     */
    public Response export(String email) {
        return export(email, false);
    }

    /**
     * Export the professional's data in json or in a json file
     *
     * @param email the professional's email
     * @param asFile indicate if the data will be export in a file
     * @return the flattened professional's graph or 404 Not Found
     */
    public Response export(String email, boolean asFile) {
        try {
            return Optional.ofNullable(professionalFacade.find(email))
                    .map(pro -> builResponse(pro, asFile))
                    .orElse(Response.status(Response.Status.NOT_FOUND)
                            .entity(
                                    Json.createObjectBuilder()
                                            .add("error", "Professional "
                                                    + email
                                                    + " not found")
                                            .build()
                            ).build());
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error exporting data of Professional " + email, ex);
        }
    }

}
