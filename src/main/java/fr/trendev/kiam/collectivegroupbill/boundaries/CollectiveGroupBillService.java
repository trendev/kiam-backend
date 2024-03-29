/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.collectivegroupbill.boundaries;

import fr.trendev.kiam.bill.boundaries.AbstractBillService;
import fr.trendev.kiam.bill.entities.BillPK;
import fr.trendev.kiam.collectivegroupbill.entities.CollectiveGroupBill;
import fr.trendev.kiam.collectivegroup.entities.CollectiveGroupPK;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.collectivegroupbill.controllers.CollectiveGroupBillFacade;
import fr.trendev.kiam.collectivegroup.controllers.CollectiveGroupFacade;
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
@Path("CollectiveGroupBill")
@RolesAllowed({"Administrator", "Professional"})
public class CollectiveGroupBillService extends AbstractBillService<CollectiveGroupBill> {

    @Inject
    CollectiveGroupBillFacade collectiveGroupBillFacade;

    @Inject
    CollectiveGroupFacade collectiveGroupFacade;

    private final Logger LOG = Logger.getLogger(
            CollectiveGroupBillService.class.
                    getName());

    public CollectiveGroupBillService() {
        super(CollectiveGroupBill.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<CollectiveGroupBill, BillPK> getFacade() {
        return collectiveGroupBillFacade;
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
    @Path("{reference}/{deliverydate}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("reference") String reference,
            @PathParam("deliverydate") long deliverydate,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        BillPK pk = new BillPK(reference, new Date(deliverydate), professional);
        LOG.log(Level.INFO, "REST request to get CollectiveGroupBill : {0}",
                collectiveGroupBillFacade.
                        prettyPrintPK(
                                pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec,
            CollectiveGroupBill entity,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);

        return super.post(e -> {
            if (e.getCollectiveGroup() == null) {
                throw new WebApplicationException(
                        "A valid CollectiveGroup must be provided !");
            }

            CollectiveGroupPK collectiveGroupPK = new CollectiveGroupPK(e.
                    getCollectiveGroup().getId(), proEmail);

            e.setCollectiveGroup(
                    Optional.ofNullable(collectiveGroupFacade.find(
                            collectiveGroupPK))
                            .map(Function.identity()).orElseThrow(()
                            -> new WebApplicationException(
                                    "CollectiveGroup " + collectiveGroupFacade.
                                            prettyPrintPK(
                                                    collectiveGroupPK)
                                    + " doesn't exist !"
                            )));

            e.getCollectiveGroup().getCollectiveGroupBills().add(e);
        },
                sec, entity, professional
        );
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response put(@Context SecurityContext sec, CollectiveGroupBill entity,
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
                CollectiveGroupBill.class,
                e -> e.getCollectiveGroup().getCollectiveGroupBills().
                        remove(e),
                reference,
                deliverydate,
                professional);
    }
}
