/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ClientBillFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.ServiceFacade;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.ws.WebServiceException;

/**
 *
 * @author jsie
 */
@Stateless
@Path("ClientBill")
public class ClientBillService extends AbstractCommonService<ClientBill, BillPK> {

    @Inject
    ClientBillFacade clientBillFacade;

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ServiceFacade serviceFacade;

    private static final Logger LOG = Logger.getLogger(ClientBillService.class.
            getName());

    public ClientBillService() {
        super(ClientBill.class);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        LOG.log(Level.INFO, "Providing the ClientBill list");
        return super.findAll(clientBillFacade);
    }

    @Path("count")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return super.count(clientBillFacade);
    }

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
        return super.find(clientBillFacade, pk, refresh);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, ClientBill entity,
            @QueryParam("professional") String professional) {

        String email = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, email,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                clientBillFacade, professionalFacade,
                ClientBill::setProfessional,
                Professional::getBills, e -> {
            /**
             * Sets the reference. Keep in mind that e is already added to the
             * Professional Bills list!
             */
            e.setReference("C-" + e.getProfessional().getUuid() + "-" + e.
                    getProfessional().getBills().stream().filter(
                            b -> b instanceof ClientBill).count());

            if (e.getDeliveryDate() == null) {
                throw new WebApplicationException(
                        "A delivery date must be provided");
            }

            if (e.getPaymentDate() != null && e.getPaymentDate().before(e.
                    getDeliveryDate())) {
                throw new WebApplicationException("Payment date " + e.
                        getPaymentDate() + " cannot be before Delivery Date "
                        + e.getDeliveryDate());
            }

            if (e.getPayments().isEmpty()) {
                throw new WebApplicationException(
                        "No payment provided with the Bill");
            }

            //Currency should be unique
            Map<String, Long> currencies = e.getPayments().stream().collect(
                    Collectors.groupingBy(Payment::getCurrency, Collectors.
                            counting()));

            if (currencies.size() != 1) {
                String errmgs = "There are multiple currencies in the payments and you should have only one currency: "
                        + currencies;
                throw new WebServiceException(errmgs);
            }

            //Total amount should be equal to the sum of the amount's payment
            int amount = e.getPayments().stream().mapToInt(Payment::getAmount).
                    sum();
            if (amount != e.getAmount()) {
                LOG.log(Level.WARNING,
                        "Total amount is {0} but the total amount computed is {1}. Amount value is now {1}",
                        new Object[]{e.getAmount(), amount});
                e.setAmount(amount);
            }
        });
    }
    //
    //    @PUT
    //    @Consumes(MediaType.APPLICATION_JSON)
    //    @Produces(MediaType.APPLICATION_JSON)
    //    public Response put(@Context SecurityContext sec, ClientBill entity,
    //            @QueryParam("professional") String professional) {
    //
    //        BillPK pk = new BillPK(entity.getId(), this.getProEmail(sec,
    //                professional));
    //
    //        LOG.log(Level.INFO, "Updating ClientBill {0}", clientBillFacade.
    //                prettyPrintPK(pk));
    //        return super.put(entity, clientBillFacade, pk, e -> {
    //            e.setName(entity.getName());
    //            e.setPrice(entity.getPrice());
    //            e.setDuration(entity.getDuration());
    //            e.setHidden(entity.isHidden());
    //            e.setBusinesses(entity.getBusinesses());
    //        });
    //    }
    //
    //    @Path("{id}")
    //    @DELETE
    //    public Response delete(@Context SecurityContext sec,
    //            @PathParam("id") Long id,
    //            @QueryParam("professional") String professional) {
    //
    //        BillPK pk = new BillPK(id, this.getProEmail(sec,
    //                professional));
    //
    //        LOG.log(Level.INFO, "Deleting ClientBill {0}", clientBillFacade.
    //                prettyPrintPK(pk));
    //        return super.delete(clientBillFacade, pk,
    //                e -> e.getProfessional().getOfferings().remove(e));
    //    }
    //
    //    @Path("{packid}/addService/offering/{offeringid}")
    //    @PUT
    //    @Produces(MediaType.APPLICATION_JSON)
    //    public Response addService(@Context SecurityContext sec,
    //            @PathParam("packid") Long packid,
    //            @PathParam("offeringid") Long offeringid,
    //            @QueryParam("professional") String professional) {
    //
    //        BillPK packPK = new BillPK(packid, this.getProEmail(sec,
    //                professional));
    //
    //        BillPK offeringPK = new BillPK(offeringid, this.getProEmail(sec,
    //                professional));
    //
    //        return super.<Service, BillPK>manageAssociation(
    //                AssociationManagementEnum.INSERT,
    //                clientBillFacade, packPK,
    //                serviceFacade,
    //                offeringPK, Service.class,
    //                (p, o) -> p.getOfferings().add(o));
    //    }
    //
    //    @Path("{packid}/removeService/offering/{offeringid}")
    //    @PUT
    //    @Produces(MediaType.APPLICATION_JSON)
    //    public Response removeService(@Context SecurityContext sec,
    //            @PathParam("packid") Long packid,
    //            @PathParam("offeringid") Long offeringid,
    //            @QueryParam("professional") String professional) {
    //
    //        BillPK packPK = new BillPK(packid, this.getProEmail(sec,
    //                professional));
    //
    //        BillPK offeringPK = new BillPK(offeringid, this.getProEmail(sec,
    //                professional));
    //
    //        return super.<Service, BillPK>manageAssociation(
    //                AssociationManagementEnum.REMOVE,
    //                clientBillFacade, packPK,
    //                serviceFacade,
    //                offeringPK, Service.class,
    //                (p, o) -> p.getOfferings().remove(o));
    //    }
    //
    //    @Path("{packid}/addClientBill/offering/{offeringid}")
    //    @PUT
    //    @Produces(MediaType.APPLICATION_JSON)
    //    public Response addClientBill(@Context SecurityContext sec,
    //            @PathParam("packid") Long packid,
    //            @PathParam("offeringid") Long offeringid,
    //            @QueryParam("professional") String professional) {
    //
    //        BillPK packPK = new BillPK(packid, this.getProEmail(sec,
    //                professional));
    //
    //        BillPK offeringPK = new BillPK(offeringid, this.getProEmail(sec,
    //                professional));
    //
    //        return super.<ClientBill, BillPK>manageAssociation(
    //                AssociationManagementEnum.INSERT,
    //                clientBillFacade, packPK,
    //                clientBillFacade,
    //                offeringPK, ClientBill.class,
    //                (p, o) -> p.getOfferings().add(o));
    //    }
    //
    //    @Path("{packid}/removeClientBill/offering/{offeringid}")
    //    @PUT
    //    @Produces(MediaType.APPLICATION_JSON)
    //    public Response removeClientBill(@Context SecurityContext sec,
    //            @PathParam("packid") Long packid,
    //            @PathParam("offeringid") Long offeringid,
    //            @QueryParam("professional") String professional) {
    //
    //        BillPK packPK = new BillPK(packid, this.getProEmail(sec,
    //                professional));
    //
    //        BillPK offeringPK = new BillPK(offeringid, this.getProEmail(sec,
    //                professional));
    //
    //        return super.<ClientBill, BillPK>manageAssociation(
    //                AssociationManagementEnum.REMOVE,
    //                clientBillFacade, packPK,
    //                clientBillFacade,
    //                offeringPK, ClientBill.class,
    //                (p, o) -> p.getOfferings().remove(o));
    //    }
}
