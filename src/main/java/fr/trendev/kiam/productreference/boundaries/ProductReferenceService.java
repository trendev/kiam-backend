/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.productreference.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.productreference.entities.ProductReference;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.productreference.controllers.ProductReferenceFacade;
import fr.trendev.kiam.exceptions.ExceptionHelper;
import java.util.List;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("ProductReference")
@RolesAllowed({"Administrator"})
public class ProductReferenceService extends AbstractCommonService<ProductReference, String> {

    @Inject
    ProductReferenceFacade productReferenceFacade;

    private final Logger LOG = Logger.getLogger(ProductReferenceService.class.
            getName());

    public ProductReferenceService() {
        super(ProductReference.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<ProductReference, String> getFacade() {
        return productReferenceFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
    }

    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @Path("{barcode}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    @RolesAllowed({"Administrator", "Professional"})
    public Response find(@PathParam("barcode") String barcode,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get ProductReference : {0}",
                barcode);
        return super.find(barcode, refresh);
    }

    @Path("search/{barcode}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Administrator", "Professional"})
    public Response search(@PathParam("barcode") String barcode) {
        LOG.log(Level.INFO, "REST request to search ProductReference : {0}",
                barcode);
        try {
            List<ProductReference> result
                    = productReferenceFacade.findFromBarcode(barcode);
            return Response.status(Response.Status.OK).
                    entity(result).build();

        } catch (Exception ex) {
            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs searching ProductReference" + barcode);
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Only Administrators are allowed to create product references using this
     * service. Other users can create a Product providing a new product
     * reference.
     *
     * @param payload the product reference to create
     * @return HTTP response
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(ProductReference payload) {
        LOG.log(Level.INFO, "Creating ProductReference {0}", super.stringify(
                payload));
        return super.post(payload, e -> {
        });
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(ProductReference payload) {
        LOG.log(Level.INFO, "Updating ProductReference {0}", payload.
                getBarcode());

        return super.put(payload, payload.getBarcode(),
                e -> {
                    e.setDescription(payload.getDescription());
                    e.setBrand(payload.getBrand());
                    e.setBusiness(payload.getBusiness());
                });
    }

    /**
     * Warning : a product reference cannot be deleted if it is linked with a
     * Product...
     *
     * @param barcode the product reference barcode
     * @return HTTP response
     */
    @Path("{barcode}")
    @DELETE
    public Response delete(@PathParam("barcode") String barcode) {
        LOG.log(Level.INFO, "Deleting ProductReference {0}", barcode);
        return super.delete(barcode, e -> {
        });
    }

}
