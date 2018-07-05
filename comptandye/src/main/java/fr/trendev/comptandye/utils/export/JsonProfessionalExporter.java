/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.EncryptionUtils;
import fr.trendev.comptandye.utils.producers.qualifiers.ProfessionalExport;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
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

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    @ProfessionalExport
    private ObjectMapper om;

    @Inject
    private EncryptionUtils encryptionUtils;

    public Response export(String email) {
        try {
            return Optional.ofNullable(professionalFacade.find(email))
                    .map(pro -> Response.ok(stringify(pro)).build())
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

    private String stringify(Professional pro) {

        try {
            String json = om.writeValueAsString(pro);
            return om.writerWithDefaultPrettyPrinter().
                    writeValueAsString(new JsonProfessionalExport(pro,
                            encryptionUtils, json));

        } catch (JsonProcessingException ex) {
            throw new WebApplicationException(
                    "Error building the Response exporting data of Professional "
                    + pro.getEmail(), ex);
        }
    }

    public Response exportAsFile(String email) {
        try {
            return Optional.ofNullable(professionalFacade.find(email))
                    .map(pro -> {
                        Response.ResponseBuilder response = Response.ok(
                                stringify(pro));
                        String date = new SimpleDateFormat("yyyyMMdd")
                                .format(new Date());
                        response.header("Content-Disposition",
                                "attachment; filename=\"backup_" + date + "_"
                                + pro.
                                        getUuid()
                                + ".json\"");
                        return response.build();
                    })
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
