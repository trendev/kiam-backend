/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.Offering;
import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ClientBillFacade;
import fr.trendev.comptandye.sessions.ClientFacade;
import fr.trendev.comptandye.sessions.PackFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.sessions.ServiceFacade;
import fr.trendev.comptandye.visitors.ProvideOfferingFacadeVisitor;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    ClientFacade clientFacade;

    @Inject
    ServiceFacade serviceFacade;

    @Inject
    PackFacade packFacade;

    @Inject
    ProvideOfferingFacadeVisitor v;

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

            if (!e.getPayments().isEmpty()) {

                //Total amount should be equal to the sum of the amount's payment
                int amount = e.getPayments().stream().mapToInt(
                        Payment::getAmount).
                        sum();

                if (amount != e.getAmount()) {
                    LOG.log(Level.WARNING,
                            "Total amount is {0} but the total amount computed is {1}",
                            new Object[]{e.getAmount(), amount});
                }
            } else {
                LOG.log(Level.INFO,
                        "ClientBill {0} delivered on {1} has not been paid : no payment provided during the Bill creation",
                        new Object[]{e.getReference(), e.
                            getDeliveryDate()});
            }

            List<PurchasedOffering> purchasedOfferings = e.
                    getPurchasedOfferings().
                    stream()
                    .map(po -> Optional.ofNullable(this.getOfferingFacade(po.
                            getOffering()).find(new OfferingPK(
                                    po.getOffering().getId(),
                                    e.getProfessional().getEmail())))
                            .map(o ->
                                    new PurchasedOffering(po.getQty(), o))
                            .orElseThrow(() ->
                                    new WebApplicationException(
                                            po.getOffering().getClass().
                                                    getSimpleName()
                                            + " " + po.getOffering().getId()
                                            + " does not exist"))
                    ).collect(Collectors.toList());

            int total = purchasedOfferings.stream()
                    .mapToInt(po -> po.getQty() * po.getOffering().getPrice())
                    .sum();

//            LOG.log(Level.INFO, "Total price = {0} {1}", new Object[]{total, e.
//                getPayments().get(0)});
            e.setPurchasedOfferings(purchasedOfferings);
        });
    }

    private AbstractFacade<? extends Offering, OfferingPK> getOfferingFacade(
            Offering o) {
        return Optional.of(o.accept(v)).get();
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
