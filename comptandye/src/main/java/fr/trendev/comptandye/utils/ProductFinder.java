/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductPK;
import fr.trendev.comptandye.sessions.ProductFacade;
import java.util.Optional;
import java.util.function.Function;
import javax.ejb.Stateless;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
@Stateless
public class ProductFinder<T> {

    public Product findProduct(T e,
            String professional,
            ProductFacade productFacade,
            Class<T> clazz,
            Function<T, Product> productGetter) {

        return productFacade.find(
                new ProductPK(professional,
                        Optional.ofNullable(productGetter.apply(e))
                                .map(p_ ->
                                        Optional.ofNullable(
                                                p_.getProductReference())
                                                .map(pr -> pr.getBarcode()).
                                                orElseThrow(
                                                        () ->
                                                        new WebApplicationException(
                                                                "A ProductReference must be provided inside the Product during "
                                                                + clazz.
                                                                        getSimpleName()
                                                                + " creation")
                                                ))
                                .orElseThrow(
                                        () ->
                                        new WebApplicationException(
                                                "A Product must be provided during "
                                                + clazz.
                                                        getSimpleName()
                                                + " creation")
                                )));
    }

}
