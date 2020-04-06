/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.productrecord.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.product.entities.Product;
import fr.trendev.kiam.product.entities.ProductPK;
import fr.trendev.kiam.productrecord.entities.ProductRecord;
import fr.trendev.kiam.product.controllers.ProductFacade;
import fr.trendev.kiam.product.controllers.ProductFinder;
import fr.trendev.kiam.productrecord.entities.VariationOfProductRecordQtyVisitor;
import java.util.Date;
import java.util.function.Consumer;
import java.util.logging.Level;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 * @param <T> subtype of ProductRecord
 */
public abstract class AbstractProductRecordService<T extends ProductRecord>
        extends AbstractCommonService<T, String> {

    @Inject
    ProductFacade productFacade;

    @Inject
    private ProductFinder<ProductRecord> productFinder;

    @Inject
    VariationOfProductRecordQtyVisitor visitor;

    public AbstractProductRecordService(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected abstract AbstractFacade<T, String> getFacade();

    public Response post(T payload,
            String professional,
            Consumer<T> createActions) {
        return super.post(payload, e -> {
            //inits the product record
            e.setId(UUIDGenerator.generateID());
            e.setRecordDate(new Date());// timestamp the record
            e.setCancelled(false);
            e.setCancellationDate(null);

            if (e.getQty() <= 0) {
                throw new WebApplicationException("(" + e.getQty()
                        + ") is not a supported value for qty field in ProductRecord: must not be less than 1");
            }

            //links the product and the current product record
            Product product = productFinder.findProduct(e,
                    professional,
                    ProductRecord.class,
                    ProductRecord::getProduct);

            if (product == null) {
                throw new WebApplicationException("Product " + payload.
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

    /**
     * This method can be only used to cancel a product record. Other fields
     * won't be updated.
     *
     * @param payload the product record modifications
     * @param professional the owner of the product record
     * @return a HTTP Response
     */
    public Response put(T payload,
            String professional) {
        return super.put(payload, payload.getId(),
                e -> {
                    if (payload.isCancelled() && !e.isCancelled()) {

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

    @Override
    public Response delete(String id, Consumer<T> deleteActions) {
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
