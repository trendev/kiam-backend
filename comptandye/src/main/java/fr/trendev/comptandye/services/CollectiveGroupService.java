/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientPK;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.CollectiveGroupPK;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ClientFacade;
import fr.trendev.comptandye.sessions.CollectiveGroupFacade;
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
@Path("CollectiveGroup")
public class CollectiveGroupService extends AbstractCommonService<CollectiveGroup, CollectiveGroupPK> {

    @Inject
    CollectiveGroupFacade collectiveGroupFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ClientFacade clientFacade;

    private static final Logger LOG = Logger.getLogger(
            CollectiveGroupService.class.
                    getName());

    public CollectiveGroupService() {
        super(CollectiveGroup.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the CollectiveGroup list");
        return super.findAll(collectiveGroupFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(collectiveGroupFacade);
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("id") Long id,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        CollectiveGroupPK pk = new CollectiveGroupPK(id, professional);
        LOG.log(Level.INFO, "REST request to get CollectiveGroup : {0}",
                collectiveGroupFacade.
                        prettyPrintPK(
                                pk));
        return super.find(collectiveGroupFacade, pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, CollectiveGroup entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                collectiveGroupFacade, professionalFacade,
                CollectiveGroup::setProfessional,
                Professional::getCollectiveGroups, e -> {
        });

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context SecurityContext sec, CollectiveGroup entity,
            @QueryParam("professional") String professional) {

        CollectiveGroupPK pk = new CollectiveGroupPK(entity.getId(), this.
                getProEmail(sec,
                        professional));

        LOG.log(Level.INFO, "Updating CollectiveGroup {0}",
                collectiveGroupFacade.
                        prettyPrintPK(pk));
        return super.put(entity, collectiveGroupFacade, pk, e -> {
            entity.getAddress().setId(e.getAddress().getId());
            e.setAddress(entity.getAddress());

            e.setGroupName(entity.getGroupName());
            e.setPhone(entity.getPhone());
        });
    }

    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") Long id,
            @QueryParam("professional") String professional) {

        CollectiveGroupPK pk = new CollectiveGroupPK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting CollectiveGroup {0}",
                collectiveGroupFacade.
                        prettyPrintPK(pk));
        return super.delete(collectiveGroupFacade, pk,
                e -> {
            e.getProfessional().getCollectiveGroups().remove(e);
            e.getClients().forEach(cl -> cl.getCollectiveGroups().remove(e));
        });
    }

    @Path("{collectiveGroupid}/addClient/{clientid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response addClient(@Context SecurityContext sec,
            @PathParam("collectiveGroupid") Long collectiveGroupid,
            @PathParam("clientid") Long clientid,
            @QueryParam("professional") String professional) {

        CollectiveGroupPK collectiveGroupPK = new CollectiveGroupPK(
                collectiveGroupid, this.getProEmail(sec,
                        professional));

        ClientPK clientPK = new ClientPK(clientid, this.getProEmail(sec,
                professional));

        return super.<Client, ClientPK>manageAssociation(
                AssociationManagementEnum.INSERT,
                collectiveGroupFacade, collectiveGroupPK,
                clientFacade,
                clientPK, Client.class,
                (cg, cl) -> cg.getClients().add(cl)
                & cl.getCollectiveGroups().
                        add(cg));
    }

    @Path("{collectiveGroupid}/removeClient/{clientid}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeClient(@Context SecurityContext sec,
            @PathParam("collectiveGroupid") Long collectiveGroupid,
            @PathParam("clientid") Long clientid,
            @QueryParam("professional") String professional) {

        CollectiveGroupPK collectiveGroupPK = new CollectiveGroupPK(
                collectiveGroupid, this.getProEmail(sec,
                        professional));

        ClientPK clientPK = new ClientPK(clientid, this.getProEmail(sec,
                professional));

        return super.<Client, ClientPK>manageAssociation(
                AssociationManagementEnum.REMOVE,
                collectiveGroupFacade, collectiveGroupPK,
                clientFacade,
                clientPK, Client.class,
                (cg, cl) -> cg.getClients().remove(cl) & cl.
                getCollectiveGroups().
                remove(cg));
    }

    @Path("{id}/clients")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getClients(@PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        CollectiveGroupPK pk = new CollectiveGroupPK(id, professional);
        LOG.log(Level.INFO,
                "REST request to get Clients of CollectiveGroup : {0}",
                collectiveGroupFacade.prettyPrintPK(pk));
        return super.provideRelation(collectiveGroupFacade,
                pk,
                CollectiveGroup::getClients);
    }

    @Path("{id}/collectiveGroupBills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCollectiveGroupBills(@PathParam("id") Long id,
            @QueryParam("professional") String professional) {
        CollectiveGroupPK pk = new CollectiveGroupPK(id, professional);
        LOG.log(Level.INFO,
                "REST request to get CollectiveGroupBills of CollectiveGroup : {0}",
                collectiveGroupFacade.prettyPrintPK(pk));
        return super.provideRelation(collectiveGroupFacade,
                pk,
                CollectiveGroup::getCollectiveGroupBills);
    }
}
