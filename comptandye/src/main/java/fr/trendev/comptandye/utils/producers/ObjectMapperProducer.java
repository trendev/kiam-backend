/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.producers;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductRecord;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Sale;
import fr.trendev.comptandye.utils.mixins.ProductMixin;
import fr.trendev.comptandye.utils.mixins.ProductRecordMixin;
import fr.trendev.comptandye.utils.mixins.ProfessionalMixin;
import fr.trendev.comptandye.utils.mixins.SaleMixin;
import fr.trendev.comptandye.utils.producers.qualifiers.ProfessionalExport;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ObjectMapperProducer {

    private final ObjectMapper om;

    public ObjectMapperProducer() {
        this.om = new ObjectMapper();
    }

    @Produces
    public ObjectMapper getDefaultObjectMapper() {
        return this.om;
    }

    @Produces
    @ProfessionalExport
    public ObjectMapper getPEObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.addMixIn(Professional.class, ProfessionalMixin.class);
        om.addMixIn(Product.class, ProductMixin.class);
        om.addMixIn(ProductRecord.class, ProductRecordMixin.class);
        om.addMixIn(Sale.class, SaleMixin.class);
        return om;
    }
}
