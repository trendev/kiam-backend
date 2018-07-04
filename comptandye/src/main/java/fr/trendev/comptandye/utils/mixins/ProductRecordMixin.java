/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.mixins;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.trendev.comptandye.entities.Product;

/**
 *
 * @author jsie
 */
public abstract class ProductRecordMixin {

    @JsonIgnore
    public abstract Product getProduct();

}
