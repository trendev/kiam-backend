/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.backups;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.utils.EncryptionSalt;
import fr.trendev.comptandye.utils.EncryptionUtils;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
public class JsonProfessionalBackup {

    private long timestamp;
    private String checksum;
    private Professional professional;

    private final Logger LOG = Logger.getLogger(JsonProfessionalBackup.class.
            getName());

    public JsonProfessionalBackup() {
    }

    public JsonProfessionalBackup(Professional professional) {
        this.timestamp = System.currentTimeMillis();
        this.professional = professional;
    }

    private String getSalt() {
        return professional.getEmail() + EncryptionSalt.PRAESTAT + timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Professional getProfessional() {
        return professional;
    }

    public void setProfessional(Professional professional) {
        this.professional = professional;
    }

    public void initChecksum(EncryptionUtils encryptionUtils,
            String json) {
        this.checksum = encryptionUtils.encrypt_SHA512_base64(
                json + getSalt());
        LOG.
                log(Level.INFO, "Checksum initialized with value : "
                        + this.checksum);
    }

    public boolean isValid(ObjectMapper om, EncryptionUtils encryptionUtils) {
        try {
            LOG.log(Level.INFO, "Validating backup of Professional "
                    + professional.getEmail());
            String json = om.writeValueAsString(this.professional);
            String checksum = encryptionUtils.encrypt_SHA512_base64(
                    json + getSalt());
            LOG.log(Level.INFO, "Checksum should be : " + checksum);
            return this.checksum.equals(checksum);
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error validating a backup of Professional "
                    + professional.getEmail(), ex);
        }

    }

}
