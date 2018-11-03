/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.backup.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.security.controllers.EncryptionMechanism;
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
        return professional.getEmail() + BackupEncryptionSalt.PRAESTAT
                + timestamp;
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

    /**
     * Inits the checksum control (with salt)
     *
     * @param encryptionUtils the encryption utils
     * @param json the flattened graph
     */
    public void initChecksum(EncryptionMechanism encryptionUtils,
            String json) {
        this.checksum = encryptionUtils.encrypt_SHA512_base64(
                json + getSalt());
        LOG.
                log(Level.INFO, "Checksum initialized with value : {0}",
                        this.checksum);
    }

    /**
     * Control if the Backup is valid or not, comparing the provided checksum
     * with the computed one.
     *
     * @param om the ObjectMapper (should be a custom ProfessionalExport)
     * @param encryptionUtils the encryption utils
     * @return true if the backup is valid or false if not
     */
    public boolean isValid(ObjectMapper om, EncryptionMechanism encryptionUtils) {
        try {
            LOG.log(Level.INFO, "Validating backup of Professional {0}",
                    professional.getEmail());
            String json = om.writeValueAsString(this.professional);
            String checksum = encryptionUtils.encrypt_SHA512_base64(
                    json + getSalt());

            boolean result = this.checksum.equals(checksum);
            if (!result) {
                LOG.log(Level.WARNING,
                        "*** CHECKSUM MISMATCH ***\nBACKUP Checksum : {0}\nCOMPUTED Checksum : {1}",
                        new Object[]{this.checksum,
                            checksum});
            } else {
                LOG.log(Level.INFO, "VALID CHECKSUM");
            }
            return result;
        } catch (Exception ex) {
            throw new WebApplicationException(
                    "Error validating a backup of Professional "
                    + professional.getEmail(), ex);
        }

    }

    private enum BackupEncryptionSalt {
        LABOR("Labor omnia vincit improbus."),
        PRAESTAT("Præstat cautela quam medela.");
        private final String value;

        private BackupEncryptionSalt(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
