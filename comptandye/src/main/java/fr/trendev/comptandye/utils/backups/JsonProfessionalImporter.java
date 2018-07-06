/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.backups;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.trendev.comptandye.utils.EncryptionUtils;
import fr.trendev.comptandye.utils.producers.qualifiers.ProfessionalExport;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
public class JsonProfessionalImporter {

    private final Logger LOG = Logger.getLogger(JsonProfessionalImporter.class.
            getName());

    @Inject
    @ProfessionalExport
    private ObjectMapper om;

    @Inject
    private EncryptionUtils encryptionUtils;

    public Response control(InputStream is) {
        try {
            JsonProfessionalBackup jpb = om.readValue(is,
                    JsonProfessionalBackup.class);
            LOG.log(Level.INFO, "Backup stream read ...");

            return Response.ok(
                    om.writeValueAsString(
                            createObjectNode(jpb)
                    ))
                    .build();
        } catch (IOException ex) {
            throw new WebApplicationException(
                    "Error controlling data", ex);
        }

    }

    protected ObjectNode createObjectNode(JsonProfessionalBackup jpb) {
        boolean isValid = jpb.isValid(om, encryptionUtils);
        ObjectNode node = om.createObjectNode();
        node.put("timestamp", jpb.getTimestamp());
        node.put("checksum", jpb.getChecksum());
        node.put("valid", isValid);
        return node;
    }

}
