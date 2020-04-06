/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.product.controllers;

import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.product.entities.ProductPK;
import java.util.Optional;
import java.util.function.Function;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 * @param <T> Sale or ProductRecord
 */
@Stateless
public class ProductFinder<T> {

    @Inject
    private ProductFacade productFacade;

    /**
     * Find a Product Entity in the EntityManager from relation target entity
     * (ProductRecord or Sale)
     *
     * @param e a ProductRecord or a Sale
     * @param professional the owner of the entities
     * @param clazz the class of the relation target entity
     * @param productGetter the getter method of the relation target entity
     * @return a ProductRecord from the EntityManager of throws
     * WebApplicationException
     */
    public Product findProduct(T e,
            String professional,
            Class<T> clazz,
            Function<T, Product> productGetter) {

        return this.productFacade.find(
                new ProductPK(professional,
                        Optional.ofNullable(productGetter.apply(e))
                                .map(p_ -> Optional.ofNullable(p_.getProductReference())
                                .map(pr -> pr.getBarcode()).
                                orElseThrow(
                                        () -> new WebApplicationException(
                                                "A ProductReference must be provided inside the Product during "
                                                + clazz.
                                                        getSimpleName()
                                                + " creation")
                                ))
                                .orElseThrow(
                                        () -> new WebApplicationException(
                                                "A Product must be provided during "
                                                + clazz.
                                                        getSimpleName()
                                                + " creation")
                                )));
    }

}
