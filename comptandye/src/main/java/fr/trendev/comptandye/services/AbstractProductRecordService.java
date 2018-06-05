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
import java.util.Date;
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
            //inits the product record
            e.setId(null);
            e.setRecordDate(new Date());
            e.setCancelled(false);
            e.setCancellationDate(null);

            if (e.getQty() <= 0) {
                throw new WebApplicationException("(" + e.getQty()
                        + ") is not a supported value for qty field in ProductRecord: must not be less than 1");
            }

            //links the product and the current product record
            Product product = this.findProduct(e, professional);

            if (product == null) {
                throw new WebApplicationException("Product " + entity.
                        getProduct().getProductReference().getBarcode()
                        + " not found for user " + professional + " !");
            }

            product.getProductRecords().add(e);
            e.setProduct(product);

            // updates availableQty
            int qty = product.getAvailableQty() + e.accept(visitor);

            product.setAvailableQty(qty < 0 ? 0 : qty);

            // performs additionnal operations
            createActions.accept(e);
        });
    }

    public Response put(T entity,
            String professional) {
        return super.put(entity, entity.getId(),
                e -> {
            if (entity.isCancelled() && !e.isCancelled()) {
                e.setCancelled(true);
                e.setCancellationDate(new Date());

                Product product = productFacade.find(
                        new ProductPK(professional,
                                e.getProduct().getProductReference().
                                        getBarcode()));

                if (product == null) {
                    throw new WebApplicationException("Product " + e.
                            getProduct().getProductReference().getBarcode()
                            + " not found for user " + professional + " !");
                }

                // updates availableQty
                int qty = product.getAvailableQty() - e.accept(visitor);
                product.setAvailableQty(qty < 0 ? 0 : qty);
            }// else, do nothing
        });
    }

    public Response delete(Long id, Consumer<T> deleteActions) {
        return super.delete(id, e -> {

            // updates availableQty in Product if the product record is not cancelled
            if (!e.isCancelled()) {
                Product product = e.getProduct();
                int qty = product.getAvailableQty() - e.accept(visitor);
                product.setAvailableQty(qty < 0 ? 0 : qty);
            }

            e.getProduct().getProductRecords().remove(e);
            e.setProduct(null);

            deleteActions.accept(e);
        });
    }

    private Product findProduct(T e,
            String professional) {

        return productFacade.find(
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
    }

}
