/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.objectmapper.backupcfg.mixins;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.trendev.comptandye.productrecord.entities.ProductRecord;
import fr.trendev.comptandye.sale.entities.Sale;
import java.util.List;

/**
 *
 * @author jsie
 */
public abstract class ProductMixin {

    @JsonProperty
    public abstract List<ProductRecord> getProductRecords();

    @JsonProperty
    public abstract List<Sale> getSales();

}
