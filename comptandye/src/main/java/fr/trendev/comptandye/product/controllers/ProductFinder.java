/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.product.controllers;

import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.product.entities.ProductPK;
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

    /**
     * Find a Product Entity in the EntityManager from relation target entity
     * (ProductRecord or Sale)
     *
     * @param e a ProductRecord or a Sale
     * @param professional the owner of the entities
     * @param productFacade the Product EJB Session
     * @param clazz the class of the relation target entity
     * @param productGetter the getter method of the relation target entity
     * @return a ProductRecord from the EntityManager of throws
     * WebApplicationException
     */
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
