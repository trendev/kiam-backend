/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.trendev.comptandye.entities.Professional;
import java.io.IOException;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
public class ProfessionalSerializer extends StdSerializer<Professional> {

    public ProfessionalSerializer() {
        super(Professional.class);
    }

    @Override
    public void serialize(
            Professional pro, JsonGenerator jgen,
            SerializerProvider serializer) {

        try {
            jgen.writeFieldName("professional");
            jgen.writeStartObject();
            jgen.writeStringField("email", pro.getEmail());
            jgen.writeEndObject();
            jgen.writeObjectField("vatRates", pro.getVatRates());
            jgen.writeObjectField("bills", pro.getBills());
            jgen.writeObjectField("businesses", pro.getBusinesses());

        } catch (IOException ex) {
            String errmsg = "Error during serialization of Professional " + pro.
                    getEmail();
            throw new WebApplicationException(errmsg, ex);
        }
    }

    @Override
    public void serializeWithType(Professional value, JsonGenerator gen,
            SerializerProvider serializers, TypeSerializer typeSer) throws
            IOException {
        typeSer.writeTypePrefixForObject(value, gen);
        serialize(value, gen, serializers); // call your customized serialize method
        typeSer.writeTypeSuffixForObject(value, gen);
    }

}
