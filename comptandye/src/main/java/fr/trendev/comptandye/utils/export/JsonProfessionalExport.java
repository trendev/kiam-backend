/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.export;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductRecord;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Sale;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.mixins.ProductMixin;
import fr.trendev.comptandye.utils.mixins.ProductRecordMixin;
import fr.trendev.comptandye.utils.mixins.ProfessionalMixin;
import fr.trendev.comptandye.utils.mixins.SaleMixin;
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
public class JsonProfessionalExport {

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    private ObjectMapper om;

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
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.addMixIn(Professional.class, ProfessionalMixin.class);
            objectMapper.addMixIn(Product.class, ProductMixin.class);
            objectMapper.addMixIn(ProductRecord.class, ProductRecordMixin.class);
            objectMapper.addMixIn(Sale.class, SaleMixin.class);
            String json = objectMapper.writerWithDefaultPrettyPrinter().
                    writeValueAsString(pro);
            System.out.println(json);
            return json;

        } catch (JsonProcessingException ex) {
            throw new WebApplicationException(
                    "Error building the Response exporting data of Professional "
                    + pro.getEmail(), ex);
        }
    }

}