/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.clientbill.boundaries;

import fr.trendev.comptandye.bill.boundaries.AbstractBillService;
import fr.trendev.comptandye.bill.entities.BillPK;
import fr.trendev.comptandye.clientbill.entities.ClientBill;
import fr.trendev.comptandye.client.entities.ClientPK;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.clientbill.controllers.ClientBillFacade;
import fr.trendev.comptandye.client.controllers.ClientFacade;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
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
@Path("ClientBill")
@RolesAllowed({"Administrator", "Professional"})
public class ClientBillService extends AbstractBillService<ClientBill> {

    @Inject
    ClientBillFacade clientBillFacade;

    @Inject
    ClientFacade clientFacade;

    private final Logger LOG = Logger.getLogger(ClientBillService.class.
            getName());

    public ClientBillService() {
        super(ClientBill.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<ClientBill, BillPK> getFacade() {
        return clientBillFacade;
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
    public Response count() {
        return super.count();
    }

    @RolesAllowed({"Administrator"})
    @Path("{reference}/{deliverydate}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("reference") String reference,
            @PathParam("deliverydate") long deliverydate,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        BillPK pk = new BillPK(reference, new Date(deliverydate), professional);
        LOG.log(Level.INFO, "REST request to get ClientBill : {0}",
                clientBillFacade.
                        prettyPrintPK(
                                pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, ClientBill entity,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);

        return super.post(e -> {
            if (e.getClient() == null) {
                throw new WebApplicationException(
                        "A valid Client must be provided !");
            }

            /**
             * The ClientBill must have the same professional than the Client of
             * the Bill. This is why we use proEmail instead of
             * e.getClient().getProfessional().getEmail()
             */
            ClientPK clientPK = new ClientPK(e.getClient().getId(), proEmail);

            e.setClient(
                    Optional.ofNullable(clientFacade.find(clientPK))
                            .map(Function.identity()).orElseThrow(() ->
                            new WebApplicationException(
                                    "Client " + clientFacade.prettyPrintPK(
                                            clientPK) + " doesn't exist !"
                            )));

            e.getClient().getClientBills().add(e);
        },
                sec, entity, professional);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response put(@Context SecurityContext sec, ClientBill entity,
            @QueryParam("professional") String professional) {
        return super.put(sec, entity, professional);
    }

    @RolesAllowed({"Administrator"})
    @Path("{reference}/{deliverydate}")
    @DELETE
    public Response delete(@PathParam("reference") String reference,
            @PathParam("deliverydate") long deliverydate,
            @QueryParam("professional") String professional) {

        return super.delete(
                ClientBill.class,
                e -> e.getClient().getClientBills().remove(e),
                reference,
                deliverydate,
                professional);
    }
}