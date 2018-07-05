/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.export;

import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.utils.EncryptionSalt;
import fr.trendev.comptandye.utils.EncryptionUtils;

/**
 *
 * @author jsie
 */
public class JsonProfessionalExport {

    private long timestamp;
    private String checksum;
    private Professional professional;

    public JsonProfessionalExport() {
    }

    public JsonProfessionalExport(Professional pro,
            EncryptionUtils encryptionUtils,
            String json) {
        this.timestamp = System.currentTimeMillis();
        this.checksum = encryptionUtils.encrypt_SHA512_base64(
                json + EncryptionSalt.PRAESTAT + timestamp);
        this.professional = pro;
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

}
