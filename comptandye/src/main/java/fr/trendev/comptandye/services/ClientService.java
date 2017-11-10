/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.ClientPK;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ClientBillFacade;
import fr.trendev.comptandye.sessions.ClientFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
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
    @Override
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the Client list");
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
            e.setId(null);
        });

    }

    @GET
    @Path("import")
    @Produces(MediaType.APPLICATION_JSON)
    public Response importEntity(@Context SecurityContext sec,
            @QueryParam("professional") String professional) {
        String email = this.getProEmail(sec, professional);

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream("csv/clients.csv");

        List<Client> list = new LinkedList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(is))) {

            in.lines().skip(1).forEach(l -> {
                String[] fields = l.split(",", -1);
                if (fields.length != 12) {
                    throw new WebApplicationException(
                            "Error during clients import : fields length shoud be 12");
                }

                Date bd = null;
                try {
                    bd = sdf.parse(fields[2]);
                } catch (ParseException ex) {
                }

                Client c = new Client(fields[7],
                        new SocialNetworkAccounts(fields[8], fields[9],
                                fields[11], fields[10]),
                        new CustomerDetails(fields[1], fields[0], null,
                                fields[3],
                                bd,
                                'F',
                                null, Collections.<String>emptyList()),
                        new Address(fields[4], null, fields[5], fields[6]),
                        new Professional());//should be vanessa
                list.add(c);
            });

        } catch (Exception ex) {
            throw new WebApplicationException("Error during clients import", ex);
        }
        return Response.ok(list).build();
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
            e.getCollectiveGroups().forEach(cg -> cg.getClients().remove(e));
            e.getCategories().forEach(ct -> ct.getClients().remove(e));
        });
    }

    @Path("{id}/clientBills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClientBills(@PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        ClientPK pk = new ClientPK(id, professional);
        return super.provideRelation(pk, Client::getClientBills,
                ClientBill.class);
    }

    @Path("{id}/collectiveGroups")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCollectiveGroups(@PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        ClientPK pk = new ClientPK(id, professional);
        return super.provideRelation(pk, Client::getCollectiveGroups,
                CollectiveGroup.class);
    }

    @Path("{id}/categories")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories(@PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        ClientPK pk = new ClientPK(id, professional);
        return super.provideRelation(pk, Client::getCategories, Category.class);
    }
}
