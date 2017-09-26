/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.sessions.PurchasedOfferingFacade;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Stateless
@Path("PurchasedOffering")
public class PurchasedOfferingService extends AbstractCommonService<PurchasedOffering, Long> {
    
    @Inject
    PurchasedOfferingFacade purchasedOfferingFacade;
    
    private static final Logger LOG = Logger.getLogger(
            PurchasedOfferingService.class.
                    getName());
    
    public PurchasedOfferingService() {
        super(PurchasedOffering.class);
    }
    
    @Override
    protected Logger getLogger() {
        return LOG;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the PurchasedOffering list");
        return super.findAll(purchasedOfferingFacade);
    }
    
    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(purchasedOfferingFacade);
    }
    
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id,
            @QueryParam("refresh") boolean refresh) {
        LOG.log(Level.INFO, "REST request to get PurchasedOffering : {0}", id);
        return super.find(purchasedOfferingFacade, id, refresh);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(PurchasedOffering entity) {
        LOG.log(Level.INFO, "Creating PurchasedOffering {0}", super.stringify(
                entity));
        
        return super.post(entity, purchasedOfferingFacade, e -> {
        });
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(PurchasedOffering entity) {
        LOG.log(Level.INFO, "Updating PurchasedOffering {0}", entity.getId());
        return super.put(entity, purchasedOfferingFacade, entity.getId(),
                e -> {
            e.setQty(entity.getQty());
            e.setOffering(entity.getOffering());
        });
    }
    
    @Path("{id}")
    @DELETE
    public Response delete(@PathParam("id") Long id) {
        LOG.log(Level.INFO, "Deleting PurchasedOffering {0}", id);
        return super.delete(purchasedOfferingFacade, id, e -> {
        });
    }
}
