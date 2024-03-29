/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.individualbill.boundaries;

import fr.trendev.kiam.bill.boundaries.AbstractBillService;
import fr.trendev.kiam.bill.entities.BillPK;
import fr.trendev.kiam.individualbill.entities.IndividualBill;
import fr.trendev.kiam.common.controllers.AbstractFacade;
import fr.trendev.kiam.individualbill.controllers.IndividualBillFacade;
import fr.trendev.kiam.individual.controllers.IndividualFacade;
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
@Path("IndividualBill")
@RolesAllowed({"Administrator"})
public class IndividualBillService extends AbstractBillService<IndividualBill> {

    @Inject
    IndividualBillFacade individualBillFacade;

    @Inject
    IndividualFacade individualFacade;

    private final Logger LOG = Logger.getLogger(
            IndividualBillService.class.
                    getName());

    public IndividualBillService() {
        super(IndividualBill.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @Override
    protected AbstractFacade<IndividualBill, BillPK> getFacade() {
        return individualBillFacade;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void findAll(@Suspended final AsyncResponse ar) {
        super.findAll(ar);
    }

    @Path("count")
    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON,})
    @Override
    public Response count() {
        return super.count();
    }

    @Path("{reference}/{deliverydate}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response find(@PathParam("reference") String reference,
            @PathParam("deliverydate") long deliverydate,
            @QueryParam("professional") String professional,
            @QueryParam("refresh") boolean refresh) {
        BillPK pk = new BillPK(reference, new Date(deliverydate), professional);
        LOG.log(Level.INFO, "REST request to get IndividualBill : {0}",
                individualBillFacade.
                        prettyPrintPK(
                                pk));
        return super.find(pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, IndividualBill entity,
            @QueryParam("professional") String professional) {

        return super.post(e -> {
            if (e.getIndividual() == null) {
                throw new WebApplicationException(
                        "A valid Individual must be provided !");
            }

            e.setIndividual(
                    Optional.ofNullable(individualFacade.find(e.getIndividual().
                            getEmail()))
                            .map(Function.identity()).orElseThrow(() ->
                            new WebApplicationException(
                                    "Individual " + individualFacade.
                                            prettyPrintPK(
                                                    e.getIndividual().getEmail())
                                    + " doesn't exist !"
                            )));

            e.getIndividual().getIndividualBills().add(e);
        },
                sec, entity, professional);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response put(@Context SecurityContext sec, IndividualBill entity,
            @QueryParam("professional") String professional) {
        return super.put(sec, entity, professional);
    }

    @Path("{reference}/{deliverydate}")
    @DELETE
    public Response delete(@PathParam("reference") String reference,
            @PathParam("deliverydate") long deliverydate,
            @QueryParam("professional") String professional) {

        return super.delete(
                IndividualBill.class,
                e -> e.getIndividual().getIndividualBills().remove(e),
                reference,
                deliverydate,
                professional);
    }
}
