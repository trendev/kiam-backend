/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.ClientPK;
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
import java.util.function.Function;
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

    private AbstractFacade<? extends Offering, OfferingPK> getOfferingFacade(
            Offering o) {
        return Optional.of(o.accept(v)).get();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context SecurityContext sec, ClientBill entity,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, proEmail,
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
                        "A delivery date must be provided !");
            }

            if (e.getPaymentDate() != null && e.getPaymentDate().before(e.
                    getDeliveryDate())) {
                throw new WebApplicationException("Payment date " + e.
                        getPaymentDate() + " cannot be before Delivery Date "
                        + e.getDeliveryDate());
            }

            if (!e.getPayments().isEmpty()) {
                if (e.getPaymentDate() != null) {
                    //Total amount should be equal to the sum of the amount's payment
                    int total = e.getPayments().stream()
                            .mapToInt(Payment::getAmount)
                            .sum();
                    if (total != e.getAmount()) {
                        String errmsg = "Amount is " + e.getAmount()
                                + " "
                                + e.getCurrency()
                                + " but the total amount computed (based on the payments) is "
                                + total
                                + " "
                                + e.getCurrency();
                        LOG.log(Level.WARNING, errmsg);
                        throw new WebApplicationException(errmsg);
                    }
                } else {
                    LOG.log(Level.INFO,
                            "ClientBill {0} delivered on {1} has not been paid : payments recorded but no payment date provided yet !",
                            new Object[]{e.getReference(), e.
                                getDeliveryDate()});
                }
            } else {
                if (e.getPaymentDate() != null) {
                    throw new WebApplicationException(
                            "A payment date is provided but there is no payment yet !");
                } else {
                    LOG.log(Level.INFO,
                            "ClientBill {0} delivered on {1} has not been paid : no payment provided during the Bill creation and no payment date provided yet !",
                            new Object[]{e.getReference(), e.
                                getDeliveryDate()});
                }
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
                                            + " does not exist !"))
                    ).collect(Collectors.toList());

            int total = purchasedOfferings.stream()
                    .mapToInt(po -> po.getQty() * po.getOffering().getPrice())
                    .sum();

            if (e.getAmount() != (total - e.getDiscount())) {
                String errmsg = "Amount is " + e.getAmount() + " "
                        + e.getCurrency()
                        + " but the total amount computed (based on the purchased offerings prices and the discount) is "
                        + "(" + total + "-" + e.getDiscount() + ") = "
                        + (total - e.getDiscount())
                        + " "
                        + e.getCurrency();
                LOG.log(Level.WARNING, errmsg);
                throw new WebApplicationException(errmsg);
            }

            e.setPurchasedOfferings(purchasedOfferings);

            if (e.getClient() == null) {
                throw new WebApplicationException(
                        "A valid Client must be provided !");
            }

            ClientPK clientPK = new ClientPK(e.getClient().getId(), proEmail);

            e.setClient(
                    Optional.ofNullable(clientFacade.find(clientPK))
                            .map(Function.identity()).orElseThrow(() ->
                            new WebApplicationException(
                                    "Client " + clientFacade.prettyPrintPK(
                                            clientPK) + " doesn't exist !"
                            )));

            e.getClient().getClientBills().add(e);
        });
    }

//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response put(@Context SecurityContext sec, ClientBill entity,
//            @QueryParam("professional") String professional) {
//        
//        BillPK pk = new BillPK(entity.getReference(), entity.getDeliveryDate(),
//                this.getProEmail(sec,
//                        professional));
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
//    @Path("{reference}/{deliverydate}")
//    @DELETE
//    public Response delete(@Context SecurityContext sec,
//            @PathParam("reference") String reference,
//            @PathParam("deliverydate") long deliverydate,
//            @QueryParam("professional") String professional) {
//
//        BillPK pk = new BillPK(reference, new Date(deliverydate), this.
//                getProEmail(sec, professional));
//
//        LOG.log(Level.INFO, "Deleting ClientBill {0}", clientBillFacade.
//                prettyPrintPK(pk));
//        return super.delete(clientBillFacade, pk,
//                e -> {
//            e.getProfessional().getBills().remove(e);
//            e.getClient().getClientBills().remove(e);
//        });
//    }
}
