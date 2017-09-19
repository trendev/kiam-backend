/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.CategoryPK;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientPK;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.CategoryFacade;
import fr.trendev.comptandye.sessions.ClientFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.AssociationManagementEnum;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CategoryService extends AbstractCommonService<Category, CategoryPK> {
    
    @Inject
    CategoryFacade categoryFacade;
    
    @Inject
    ProfessionalFacade professionalFacade;
    
    @Inject
    ClientFacade clientFacade;
    
    private static final Logger LOG = Logger.getLogger(CategoryService.class.
            getName());
    
    public CategoryService() {
        super(Category.class);
    }
    
    @Override
    protected Logger getLogger() {
        return LOG;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Category list");
        return super.findAll(categoryFacade);
    }
    
    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(categoryFacade);
    }
    
    @Path("key")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@QueryParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        CategoryPK pk = new CategoryPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Category : {0}",
                categoryFacade.
                        prettyPrintPK(
                                pk));
        return super.find(categoryFacade, pk, refresh);
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
                categoryFacade, professionalFacade, Category::setProfessional,
                Professional::getCategories, e -> {
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
        return super.put(entity, categoryFacade, pk, e -> {
            e.setDescription(entity.getDescription());
            e.setName(entity.getName());
        });
    }
    
    @Path("key")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @QueryParam("id") Long id,
            @QueryParam("professional") String professional) {
        
        CategoryPK pk = new CategoryPK(id, this.getProEmail(sec,
                professional));
        
        LOG.log(Level.INFO, "Deleting Category {0}", categoryFacade.
                prettyPrintPK(pk));
        return super.delete(categoryFacade, pk,
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
                categoryFacade, categoryPK,
                clientFacade,
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
                categoryFacade, categoryPK,
                clientFacade,
                clientPK, Client.class,
                (cat, cl) -> cat.getClients().remove(cl) & cl.getCategories().
                remove(cat));
    }
}
