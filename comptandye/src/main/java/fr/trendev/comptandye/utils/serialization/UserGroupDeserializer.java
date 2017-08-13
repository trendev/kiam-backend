/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.trendev.comptandye.entities.UserGroup;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
 * @param <T>
 */
public class UserGroupDeserializer extends StdDeserializer<UserGroup> {

    public UserGroupDeserializer() {
        this(null);
    }

    public UserGroupDeserializer(Class<UserGroup> t) {
        super(t);
    }

    @Override
    public UserGroup deserialize(JsonParser jp, DeserializationContext dc)
            throws IOException, JsonProcessingException {

        Logger.getLogger(UserGroupDeserializer.class.getName()).
                log(Level.INFO, "Deserializing object {0}",
                        UserGroupDeserializer.class);

        UserGroup userGroup = new UserGroup();
        JsonNode node = jp.readValueAsTree();

        node.fields().forEachRemaining(e -> {
            try {
                Field f = userGroup.getClass().getDeclaredField(e.getKey());
                f.setAccessible(true);
                f.set(userGroup,
                        this.getRealValue(f.getType(), e));

                f.setAccessible(false);
            } catch (NoSuchFieldException | SecurityException |
                    IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(UserGroupDeserializer.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });

        return userGroup;
    }

    private Object getRealValue(Class<?> clazz, Map.Entry<String, JsonNode> e) {
        if (clazz == String.class) {
            return e.getValue().asText();
        }
        if (clazz == List.class) {
            return Collections.emptyList();
        }
        return null;
    }

}
