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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jsie
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

        Logger.getLogger(
                UserGroupDeserializer.class.getName()).
                log(Level.INFO, "Deserializing object {0}",
                        UserGroupDeserializer.class);

        UserGroup userGroup = new UserGroup();

        JsonNode node = jp.readValueAsTree();

        node.fields().forEachRemaining(e -> {
            try {
                Field f = UserGroup.class.getDeclaredField(e.getKey());
                f.setAccessible(true);
                Class<?> type = f.getType();
                Object value = null;

                System.out.println("TYPE: " + type.toGenericString() + " / "
                        + e.getValue().asText());
// TODO : use polymorphic method instead...
                switch (type.toGenericString()) {
                    case "public final class java.lang.String":
                        value = e.getValue().asText();
                        break;
                    case "public abstract interface java.util.List<E>":
                        value = Collections.emptyList();
                        break;
                    default:
                        System.err.println("Incorrect type");
                        break;
                }

                f.set(userGroup,
                        value);
                f.setAccessible(false);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(UserGroupDeserializer.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(UserGroupDeserializer.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(UserGroupDeserializer.class.getName()).
                        log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(UserGroupDeserializer.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
        });
        return userGroup;
    }

}
