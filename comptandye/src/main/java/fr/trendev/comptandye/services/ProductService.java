/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductPK;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ProductFacade;
import fr.trendev.comptandye.sessions.ProductRecordFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.SaleFacade;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
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
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Product list");
        return super.findAll();
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Product entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade, Product::setProfessional,
                Professional::getStock, e -> {
            e.setAvailableQty(0);

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
        });

    }

    /*@PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Category entity,
            @QueryParam("professional") String professional) {

        CategoryPK pk = new CategoryPK(entity.getId(), this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Updating Category {0}", productFacade.
                prettyPrintPK(pk));
        return super.put(entity, pk, e -> {
            e.setDescription(entity.getDescription());
            e.setName(entity.getName());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        CategoryPK pk = new CategoryPK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Category {0}", productFacade.
                prettyPrintPK(pk));
        return super.delete(pk,
                e -> {
            e.getProfessional().getCategories().remove(e);
            e.getClients().forEach(cl -> cl.getCategories().remove(e));
        });
    }

    @Path("{categoryid}/addClient/{clientid}")
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

    @Path("{id}/clients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        CategoryPK pk = new CategoryPK(id, this.getProEmail(sec, professional));
        return super.provideRelation(pk, Category::getClients, Client.class);
    }
     */
}
