/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.objectmapper;

import fr.trendev.comptandye.objectmapper.backupcfg.ProfessionalBackup;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.productrecord.entities.ProductRecord;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.sale.entities.Sale;
import fr.trendev.comptandye.objectmapper.backupcfg.mixins.ProductMixin;
import fr.trendev.comptandye.objectmapper.backupcfg.mixins.ProductRecordMixin;
import fr.trendev.comptandye.objectmapper.backupcfg.mixins.ProfessionalMixin;
import fr.trendev.comptandye.objectmapper.backupcfg.mixins.SaleMixin;
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
        // TODO :  investigate on the serialization issue 
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.addMixIn(Professional.class, ProfessionalMixin.class);
//        objectMapper.addMixIn(Product.class, ProductMixin.class);
//        objectMapper.addMixIn(ProductRecord.class, ProductRecordMixin.class);
//        objectMapper.addMixIn(Sale.class, SaleMixin.class);
//        return objectMapper;
        return this.om;
    }
}
