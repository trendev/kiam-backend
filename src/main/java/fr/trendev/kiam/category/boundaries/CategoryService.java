/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.category.boundaries;

import fr.trendev.kiam.category.controllers.CategoryFacade;
import fr.trendev.kiam.category.entities.Category;
import fr.trendev.kiam.category.entities.CategoryPK;
import fr.trendev.kiam.client.controllers.ClientFacade;
import fr.trendev.kiam.client.entities.Client;
import fr.trendev.kiam.client.entities.ClientPK;
import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.common.boundaries.AssociationManagementEnum;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.professional.controllers.ProfessionalFacade;
import fr.trendev.kiam.professional.entities.Professional;
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
@Path("Category")
@RolesAllowed({"Administrator", "Professional"})
public class CategoryService extends AbstractCommonService<Category, CategoryPK> {

    @Inject
    CategoryFacade categoryFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ClientFacade clientFacade;

    private final Logger LOG = Logger.getLogger(CategoryService.class.
            getName());

    public CategoryService() {
        super(Category.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Category, CategoryPK> getFacade() {
        return categoryFacade;
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
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") String id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        CategoryPK pk = new CategoryPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Category : {0}",
                categoryFacade.
                        prettyPrintPK(
                                pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Category entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade, Category::setProfessional,
                Professional::getCategories, e -> {
                    e.setId(UUIDGenerator.generateID());
                });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Category entity,
            @QueryParam("professional") String professional) {

        CategoryPK pk = new CategoryPK(entity.getId(), this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Updating Category {0}", categoryFacade.
                prettyPrintPK(pk));
        return super.put(entity, pk, e -> {
            e.setDescription(entity.getDescription());
            e.setName(entity.getName());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") String id,
            @QueryParam("professional") String professional) {

        CategoryPK pk = new CategoryPK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Category {0}", categoryFacade.
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
            @PathParam("categoryid") String categoryid,
            @PathParam("clientid") String clientid,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);

        CategoryPK categoryPK = new CategoryPK(categoryid, proEmail);

        ClientPK clientPK = new ClientPK(clientid, proEmail);

        return super.<Client, ClientPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                categoryPK,
                clientFacade,
                clientPK, Client.class,
                (cat, cl)
                -> cat.getClients().add(cl) & cl.getCategories().add(cat));
    }

    @Path("{categoryid}/removeClient/{clientid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeClient(@Context SecurityContext sec,
            @PathParam("categoryid") String categoryid,
            @PathParam("clientid") String clientid,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);

        CategoryPK categoryPK = new CategoryPK(categoryid, proEmail);

        ClientPK clientPK = new ClientPK(clientid, proEmail);

        return super.<Client, ClientPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                categoryPK,
                clientFacade,
                clientPK, Client.class,
                (cat, cl)
                -> cat.getClients().remove(cl) & cl.getCategories().remove(cat));
    }

    @Path("{id}/clients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getClients(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("id") String id,
            @QueryParam("professional") String professional) {
        CategoryPK pk = new CategoryPK(id, this.getProEmail(sec, professional));
        super.provideRelation(ar, pk, Category::getClients, Client.class);
    }
}
