/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.objectmapper;

import fr.trendev.kiam.objectmapper.backupcfg.ProfessionalBackup;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.productrecord.entities.ProductRecord;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.sale.entities.Sale;
import fr.trendev.kiam.objectmapper.backupcfg.mixins.ProductMixin;
import fr.trendev.kiam.objectmapper.backupcfg.mixins.ProductRecordMixin;
import fr.trendev.kiam.objectmapper.backupcfg.mixins.ProfessionalMixin;
import fr.trendev.kiam.objectmapper.backupcfg.mixins.SaleMixin;
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
    @ProfessionalBackup
    public ObjectMapper getProfessionalBackupObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Professional.class, ProfessionalMixin.class);
        objectMapper.addMixIn(Product.class, ProductMixin.class);
        objectMapper.addMixIn(ProductRecord.class, ProductRecordMixin.class);
        objectMapper.addMixIn(Sale.class, SaleMixin.class);
        return objectMapper;
    }
}
