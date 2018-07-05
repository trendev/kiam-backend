/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.EncryptionUtils;
import fr.trendev.comptandye.utils.producers.qualifiers.ProfessionalExport;
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
                                            .add("errmsg", "Professional "
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

            long timestamp = System.currentTimeMillis();
            String proJson = om.writeValueAsString(pro);
            ObjectNode objnode = om.createObjectNode();
            objnode.put("timestamp", timestamp);
            objnode.put("checksum", encryptionUtils.encrypt_SHA512_base64(
                    proJson + timestamp));
            objnode.putPOJO("professional_details", pro);

            String json = om.writerWithDefaultPrettyPrinter().
                    writeValueAsString(objnode);
            System.out.println((System.currentTimeMillis() - timestamp) + "ms");
            return json;

        } catch (JsonProcessingException ex) {
            throw new WebApplicationException(
                    "Error building the Response exporting data of Professional "
                    + pro.getEmail(), ex);
        }
    }

}
