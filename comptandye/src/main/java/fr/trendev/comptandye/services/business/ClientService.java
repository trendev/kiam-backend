/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services.business;

import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.CategoryPK;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.ClientPK;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.sessions.CategoryFacade;
import fr.trendev.comptandye.sessions.ClientBillFacade;
import fr.trendev.comptandye.sessions.ClientFacade;
import fr.trendev.comptandye.sessions.CollectiveGroupFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
@Path("Client")
@RolesAllowed({"Administrator", "Professional"})
public class ClientService extends AbstractCommonService<Client, ClientPK> {

    @Inject
    ClientFacade clientFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ClientBillFacade clientBillFacade;

    @Inject
    CollectiveGroupFacade collectiveGroupFacade;

    @Inject
    CategoryFacade categoryFacade;

    private final Logger LOG = Logger.getLogger(ClientService.class.
            getName());

    public ClientService() {
        super(Client.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<Client, ClientPK> getFacade() {
        return clientFacade;
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
    public Response find(@PathParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        ClientPK pk = new ClientPK(id, professional);
        LOG.log(Level.INFO, "REST request to get Client : {0}",
                clientFacade.
                        prettyPrintPK(
                                pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, Client entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade,
                Client::setProfessional,
                Professional::getClients, e -> {
            e.setId(null); //ignores the id provided

            /**
             * Merge the categories with the existing and throw an Exception if
             * a category is not owned or found
             */
            List<Category> categories =
                    entity.getCategories().stream()
                            .map(ct -> Optional.ofNullable(
                                    categoryFacade.find(
                                            new CategoryPK(ct.getId(),
                                                    email)))
                                    .map(Function.identity())
                                    .orElseThrow(() ->
                                            new WebApplicationException(
                                                    "Category " + ct.
                                                            getId()
                                                    + " has not been found and cannot be added during Client creation"))
                            )
                            .collect(Collectors.toList());

            // complete the relationship with Category
            categories.forEach(ct -> ct.getClients().add(e));
            e.setCategories(categories);
        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, Client entity,
            @QueryParam("professional") String professional) {

        ClientPK pk = new ClientPK(entity.getId(), this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Updating Client {0}",
                clientFacade.
                        prettyPrintPK(pk));
        return super.put(entity, pk, e -> {
            /**
             * Will automatically ignore the id of the provided object : avoid
             * to hack another object swapping the current saved (or not) object
             * by an existing one.
             */
            entity.getCustomerDetails().setId(null);
            entity.getAddress().setId(null);
            entity.getSocialNetworkAccounts().setId(null);

            e.setCustomerDetails(entity.getCustomerDetails());
            e.setAddress(entity.getAddress());
            e.setSocialNetworkAccounts(entity.getSocialNetworkAccounts());

            e.setEmail(entity.getEmail());

            /**
             * Merge the categories with the existing and throw an Exception if
             * a category is not owned or found
             */
            List<Category> categories =
                    entity.getCategories().stream()
                            .map(ct -> Optional.ofNullable(
                                    categoryFacade.find(
                                            new CategoryPK(ct.getId(),
                                                    pk.getProfessional())))
                                    .map(Function.identity())
                                    .orElseThrow(() ->
                                            new WebApplicationException(
                                                    "Category " + ct.
                                                            getId()
                                                    + " has not been found and cannot be added during Client creation"))
                            )
                            .collect(Collectors.toList());

            //Reset the previous relationship
            e.getCategories().forEach(ct -> ct.getClients().remove(e));
            // complete the relationship with Category
            categories.forEach(ct -> ct.getClients().add(e));
            e.setCategories(categories);
        });
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        ClientPK pk = new ClientPK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting Client {0}",
                clientFacade.
                        prettyPrintPK(pk));
        return super.delete(pk,
                e -> {
            e.getProfessional().getClients().remove(e);
            e.getProfessional().getBills().removeAll(e.getClientBills());
            e.getCategories().forEach(ct -> ct.getClients().remove(e));
        });
    }

    @Path("{id}/clientBills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getClientBills(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        ClientPK pk = new ClientPK(id, this.getProEmail(sec, professional));
        super.provideRelation(ar, pk, Client::getClientBills,
                ClientBill.class);
    }

    @Path("{id}/categories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getCategories(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        ClientPK pk = new ClientPK(id, this.getProEmail(sec, professional));
        super.provideRelation(ar, pk, Client::getCategories, Category.class);
    }
}
