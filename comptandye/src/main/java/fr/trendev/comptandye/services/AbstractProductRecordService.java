/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductPK;
import fr.trendev.comptandye.entities.ProductRecord;
import fr.trendev.comptandye.sessions.ProductFacade;
import fr.trendev.comptandye.utils.ProductFinder;
import fr.trendev.comptandye.utils.visitors.VariationOfProductRecordQtyVisitor;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Level;
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
    private ProductFinder<ProductRecord> productFinder;

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
            Product product = productFinder.findProduct(e,
                    professional,
                    productFacade,
                    ProductRecord.class,
                    ProductRecord::getProduct);

            if (product == null) {
                throw new WebApplicationException("Product " + entity.
                        getProduct().getProductReference().getBarcode()
                        + " not found for user " + professional + " !");
            }

            product.getProductRecords().add(e);
            e.setProduct(product);

            // updates availableQty
            product.setAvailableQty(
                    product.getAvailableQty() + e.accept(visitor));

            // performs additionnal operations
            createActions.accept(e);
        });
    }

    public Response put(T entity,
            String professional) {
        return super.put(entity, entity.getId(),
                e -> {
            if (entity.isCancelled() && !e.isCancelled()) {

                //should come from the cache
                Product product = productFacade.find(
                        new ProductPK(professional,
                                e.getProduct().getProductReference().
                                        getBarcode()));

                // controls the Product and ProductRecord owner
                if (product == null
                        || e.getProduct() == null
                        || !e.getProduct().equals(product)) {
                    throw new WebApplicationException(
                            "A valid Product must be provided !");
                }

                e.cancel(visitor);

                this.getLogger().log(Level.INFO,
                        "ProductRecord {0} is now cancelled", this.
                                getFacade().prettyPrintPK(e.getId()));
            }// else, do nothing
        });
    }

    public Response delete(Long id, Consumer<T> deleteActions) {
        return super.delete(id, e -> {

            // updates availableQty in Product if the product record is not cancelled
            if (!e.isCancelled()) {
                e.cancel(visitor);
            }

            e.getProduct().getProductRecords().remove(e);
            e.setProduct(null);

            deleteActions.accept(e);
        });
    }

}
