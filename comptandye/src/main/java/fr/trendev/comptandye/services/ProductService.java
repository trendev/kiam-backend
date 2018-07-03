/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductPK;
import fr.trendev.comptandye.entities.ProductRecord;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Sale;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ProductFacade;
import fr.trendev.comptandye.sessions.ProductRecordFacade;
import fr.trendev.comptandye.sessions.ProductReferenceFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.SaleFacade;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Product")
@RolesAllowed({"Administrator", "Professional"})
public class ProductService extends AbstractCommonService<Product, ProductPK> {

    @Inject
    ProductFacade productFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    SaleFacade saleFacade;

    @Inject
    ProductRecordFacade productRecordFacade;

    @Inject
    ProductReferenceFacade productReferenceFacade;

    private final Logger LOG = Logger.getLogger(ProductService.class.
            getName());

    public ProductService() {
        super(Product.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Product, ProductPK> getFacade() {
        return productFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
    }

    @RolesAllowed({"Administrator"})
    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @RolesAllowed({"Administrator"})
    @Path("{productReference}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("productReference") String productReference,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        ProductPK pk = new ProductPK(professional, productReference);
        LOG.log(Level.INFO, "REST request to get Product : {0}",
                productFacade.prettyPrintPK(pk));
        return super.find(pk, refresh);
    }

    /**
     * Checks the Thresholds, set available quantity to 0 and persist the
     * Product (and the provided ProductReference if necessary)
     *
     * @param sec the security context
     * @param entity the entity to persist
     * @param professional the email of the professional (for Administrator
     * usage)
     * @param iaq ignore available quantity flag, if yes, set availableQty to 0
     * @return the persisted Product if created or an HttpError
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Product entity,
            @QueryParam("professional") String professional,
            @QueryParam("iaq") boolean iaq) {

        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade, Product::setProfessional,
                Professional::getStock, e -> {

            //ignores the provided available quantity
            if (iaq) {
                e.setAvailableQty(0);
            }

            if (entity.getThresholdWarning() < 0) {
                throw new WebApplicationException(
                        "Warning Threshold must not be less than 0");
            }

            if (entity.getThresholdSevere() < 0) {
                throw new WebApplicationException(
                        "Severe Threshold must not be less than 0");
            }

            if (entity.getThresholdSevere() > entity.getThresholdWarning()) {
                throw new WebApplicationException(
                        "Warning Threshold must not be less than Severe Threshold");
            }

            // avoid to PERSIST an existing ProductReference and use it instead
            Optional.ofNullable(productReferenceFacade
                    .find(entity.getProductReference().getBarcode()))
                    .ifPresent(pr -> e.setProductReference(pr));
            // or else let JPA persisting the provided ProductReference
        });

    }

    /**
     * Update a Product. Doesn't update the available quantity.
     *
     * @param sec
     * @param entity
     * @param professional
     * @return
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Product entity,
            @QueryParam("professional") String professional) {

        ProductPK pk = new ProductPK(
                this.getProEmail(sec, professional),
                entity.getProductReference().getBarcode());

        LOG.log(Level.INFO, "Updating Product {0}", productFacade.
                prettyPrintPK(pk));

        return super.put(entity, pk, e -> {
            e.setThresholdWarning(entity.getThresholdWarning());
            e.setThresholdSevere(entity.getThresholdSevere());
            e.setComments(entity.getComments());
        });
    }

    /**
     * Delete a Product, if sales & productRecords are empty
     *
     * @param sec the security context
     * @param barcode the code of the Product
     * @param professional the professional who owns the Product
     * @return OK or a HttpError
     */
    @Path("{barcode}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("barcode") String barcode,
            @QueryParam("professional") String professional) {

        ProductPK pk = new ProductPK(
                this.getProEmail(sec, professional), barcode);

        LOG.log(Level.INFO, "Deleting Product {0}", productFacade.
                prettyPrintPK(pk));
        return super.delete(pk,
                e -> {
            e.getProfessional().getStock().remove(e);

            if (e.getSales() != null && !e.getSales().isEmpty()) {
                throw new WebApplicationException(
                        "Product cannot be deleted because Sales are linked with this Product");
            }

            if (e.getProductRecords() != null && !e.getProductRecords().
                    isEmpty()) {
                throw new WebApplicationException(
                        "Product cannot be deleted because it has some ProductRecords");
            }
        });
    }

    /**
     * Get the Sales linked to this Product.
     *
     * @param sec the security context
     * @param barcode the code of the Product
     * @param professional the professional, owner of the Product
     * @return the sales list if OK or a HttpError
     */
    @Path("{barcode}/sales")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getSales(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("barcode") String barcode,
            @QueryParam("professional") String professional) {
        ProductPK pk = new ProductPK(
                this.getProEmail(sec, professional), barcode);
        super.provideRelation(ar, pk, Product::getSales, Sale.class);
    }

    @Path("{barcode}/productRecords")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getProductRecords(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("barcode") String barcode,
            @QueryParam("professional") String professional) {
        ProductPK pk = new ProductPK(
                this.getProEmail(sec, professional), barcode);
        super.provideRelation(ar, pk, Product::getProductRecords,
                ProductRecord.class);
    }

    /*@Path("{categoryid}/addClient/{clientid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addClient(@Context SecurityContext sec,
            @PathParam("categoryid") Long categoryid,
            @PathParam("clientid") Long clientid,
            @QueryParam("professional") String professional) {

        CategoryPK categoryPK = new CategoryPK(categoryid, this.getProEmail(sec,
                professional));

        ClientPK clientPK = new ClientPK(clientid, this.getProEmail(sec,
                professional));

        return super.<Client, ClientPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                categoryPK,
                saleFacade,
                clientPK, Client.class,
                (cat, cl) -> cat.getClients().add(cl) & cl.getCategories().
                add(cat));
    }

    @Path("{categoryid}/removeClient/{clientid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeClient(@Context SecurityContext sec,
            @PathParam("categoryid") Long categoryid,
            @PathParam("clientid") Long clientid,
            @QueryParam("professional") String professional) {

        CategoryPK categoryPK = new CategoryPK(categoryid, this.getProEmail(sec,
                professional));

        ClientPK clientPK = new ClientPK(clientid, this.getProEmail(sec,
                professional));

        return super.<Client, ClientPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                categoryPK,
                saleFacade,
                clientPK, Client.class,
                (cat, cl) -> cat.getClients().remove(cl) & cl.getCategories().
                remove(cat));
    }

    
     */
}
