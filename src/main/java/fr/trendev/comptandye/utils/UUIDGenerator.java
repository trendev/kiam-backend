/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import java.util.UUID;
import javax.ejb.Stateless;

/**
 *
 * @author jsie
 */
@Stateless
public class UUIDGenerator {

    public String generate() {
        return UUID.randomUUID().toString();
    }

    public String generate(boolean compact) {
        String uuid = generate();
        return compact ? uuid.replaceAll("-", "") : uuid;
    }

    public String generate(String header, boolean compact) {
        String uuid = generate(compact);
        return (header != null) ? header.concat(uuid) : uuid;
    }

    /**
     * Re-order UUID outputs in order to improve indexes performances
     *
     * @return an ID
     */
    public String generateID() {
        String uuid = this.generate();
        String[] values = uuid.split("-");

        if (values == null || values.length == 0) {
            throw new IllegalStateException("UUID does not contain any dash characters uuid=[" + uuid + "]");
        }

        StringBuilder id = new StringBuilder(values.length);

        // put the third value (begin with the UUID version) at the beginning
        id.append(values[2]);
        for (int i = 0; i < values.length; i++) {
            if (i != 2) {
                id.append(values[i]);
            }
        }

        return id.toString();
    }

}
