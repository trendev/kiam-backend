/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.trendev.comptandye.entities.UserGroup;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 */
public class UserGroupSerializer extends StdSerializer<UserGroup> {

    public UserGroupSerializer() {
        this(null);
    }

    public UserGroupSerializer(Class<UserGroup> t) {
        super(t);
    }

    @Override
    public void serialize(UserGroup t, JsonGenerator jg, SerializerProvider sp)
            throws IOException {

        Logger.getLogger(UserGroupSerializer.class.getName()).
                log(Level.INFO, "Serializing {0}", t.getName());

        jg.writeStartObject();
        jg.writeStringField("name", t.getName());
        jg.writeStringField("description", t.getDescription());
        jg.writeArrayFieldStart("userAccounts");
        t.getUserAccounts().forEach(u -> {
            try {
                jg.writeString(u.getEmail());
            } catch (IOException ex) {
                Logger.getLogger(UserGroupSerializer.class.getName()).
                        log(Level.SEVERE, "Error serializing "
                                + UserGroupSerializer.class
                                + ": impossible to serialize " + u.getEmail(),
                                ex);
            }
        });
        jg.writeEndArray();
        jg.writeEndObject();
    }

}
