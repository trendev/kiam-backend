/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.entities.Purchase;
import fr.trendev.comptandye.entities.PurchasedItem;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.PurchaseFacade;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
@Stateless
@Path("Purchase")
@RolesAllowed({"Administrator", "Professional"})
public class PurchaseService extends AbstractExpenseService<Purchase> {

    @Inject
    private PurchaseFacade purchaseFacade;

    private final Logger LOG = Logger.getLogger(PurchaseService.class.
            getName());

    public PurchaseService() {
        super(Purchase.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Purchase, ExpensePK> getFacade() {
        return purchaseFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Purchase list");
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
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        ExpensePK pk = new ExpensePK(id, professional);
        LOG.log(Level.INFO, "REST request to get Purchase : {0}",
                purchaseFacade.prettyPrintPK(pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response post(@Context SecurityContext sec, Purchase entity,
            @QueryParam("professional") String professional) {
        return super.post(sec, entity, professional);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * Amount, Currency, Payments and ExpenseItems/vatInclusive cannot be
     * updated using this method.
     */
    public Response put(@Context SecurityContext sec, Purchase entity,
            @QueryParam("professional") String professional) {

        return super.put(e -> {
            e.setDescription(entity.getDescription());
            e.setPaymentDate(entity.getPaymentDate());
            e.setProvider(entity.getProvider());
            e.setBusinesses(entity.getBusinesses());
        },
                sec, entity, professional);
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        ExpensePK pk = new ExpensePK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Purchase {0}", purchaseFacade.
                prettyPrintPK(pk));
        return super.delete(pk, e -> {
            e.getProfessional().getExpenses().remove(e);
            e.getPurchasedItems().forEach(pi -> pi.setPurchase(null));
            e.setPurchasedItems(null);
        });
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}/purchasedItems")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPurchasedItems(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        ExpensePK pk = new ExpensePK(id, this.getProEmail(sec,
                professional));

        return this.provideRelation(pk,
                Purchase::getPurchasedItems, PurchasedItem.class);
    }

}
