/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductPK;
import fr.trendev.comptandye.entities.ProductRecord;
import fr.trendev.comptandye.sessions.ProductFacade;
import fr.trendev.comptandye.utils.visitors.VariationOfProductRecordQtyVisitor;
import java.util.Optional;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
public abstract class AbstractProductRecordService<T extends ProductRecord>
        extends AbstractCommonService<T, Long> {

    @Inject
    ProductFacade productFacade;

    @Inject
    VariationOfProductRecordQtyVisitor visitor;

    public AbstractProductRecordService(Class<T> entityClass) {
        super(entityClass);
    }

    public Response post(T entity,
            String professional,
            Consumer<T> createActions) {
        return super.post(entity, e -> {
            e.setId(null);

            //links the product and the current product record
            Product product = productFacade.find(
                    new ProductPK(professional,
                            Optional.ofNullable(e.getProduct())
                                    .map(p_ ->
                                            Optional.ofNullable(
                                                    p_.getProductReference())
                                                    .map(pr -> pr.getBarcode()).
                                                    orElseThrow(
                                                            () ->
                                                            new WebApplicationException(
                                                                    "A ProductReference must be provided inside the Product during ProductRecord creation")
                                                    ))
                                    .orElseThrow(
                                            () ->
                                            new WebApplicationException(
                                                    "A Product must be provided during ProductRecord creation")
                                    )));

            if (product == null) {
                throw new WebApplicationException("Product " + entity.
                        getProduct().getProductReference().getBarcode()
                        + " not found for user " + professional + " !");
            }

            product.getProductRecords().add(e);
            e.setProduct(product);

            // updates availableQty
            // TODO: do the opposite on CANCEL/DELETE
            product.setAvailableQty(
                    product.getAvailableQty() + e.accept(visitor));

            // performs additionnal operations
            createActions.accept(e);
        });
    }

}
