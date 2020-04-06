/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.collectivegroup.boundaries;

import fr.trendev.kiam.common.boundaries.AbstractCommonService;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroup;
import fr.trendev.kiam.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroupPK;
import fr.trendev.kiam.professional.entities.Professional;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.client.controllers.ClientFacade;
import fr.trendev.kiam.collectivegroup.controllers.CollectiveGroupFacade;
import fr.trendev.kiam.professional.controllers.ProfessionalFacade;
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
@Path("CollectiveGroup")
@RolesAllowed({"Administrator", "Professional"})
public class CollectiveGroupService extends AbstractCommonService<CollectiveGroup, CollectiveGroupPK> {

    @Inject
    CollectiveGroupFacade collectiveGroupFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ClientFacade clientFacade;

    private final Logger LOG = Logger.getLogger(
            CollectiveGroupService.class.
                    getName());

    public CollectiveGroupService() {
        super(CollectiveGroup.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<CollectiveGroup, CollectiveGroupPK> getFacade() {
        return collectiveGroupFacade;
    }

    @RolesAllowed({"Administrator"})
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
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
        CollectiveGroupPK pk = new CollectiveGroupPK(id, professional);
        LOG.log(Level.INFO, "REST request to get CollectiveGroup : {0}",
                collectiveGroupFacade.
                        prettyPrintPK(
                                pk));
        return super.find(pk, refresh);
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
                professionalFacade,
                CollectiveGroup::setProfessional,
                Professional::getCollectiveGroups, e -> {
                    e.setId(UUIDGenerator.generateID());
                    e.getAddress().setId(UUIDGenerator.generateID());
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
        return super.put(entity, pk, e -> {
            /**
             * Address should not be null and its ID has been set during the
             * creation (POST)
             */
            // set a new id for security reason
            entity.getAddress().setId(UUIDGenerator.generateID());
            e.setAddress(entity.getAddress());

            e.setGroupName(entity.getGroupName());
            e.setPhone(entity.getPhone());
        });
    }

    @RolesAllowed({"Administrator"})
    @Path("{id}")
    @DELETE
    public Response delete(@Context SecurityContext sec,
            @PathParam("id") String id,
            @QueryParam("professional") String professional) {

        CollectiveGroupPK pk = new CollectiveGroupPK(id, this.getProEmail(sec,
                professional));

        LOG.log(Level.INFO, "Deleting CollectiveGroup {0}",
                collectiveGroupFacade.
                        prettyPrintPK(pk));
        return super.delete(pk,
                e -> {
                    e.getProfessional().getCollectiveGroups().remove(e);
                });
    }

    @Path("{id}/collectiveGroupBills")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getCollectiveGroupBills(@Suspended final AsyncResponse ar,
            @Context SecurityContext sec,
            @PathParam("id") String id,
            @QueryParam("professional") String professional) {
        CollectiveGroupPK pk = new CollectiveGroupPK(id, this.getProEmail(sec,
                professional));
        super.provideRelation(ar, pk,
                CollectiveGroup::getCollectiveGroupBills,
                CollectiveGroupBill.class);
    }
}
