/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.services;

import fr.trendev.comptandye.entities.Bill;
import fr.trendev.comptandye.entities.BillPK;
import fr.trendev.comptandye.entities.OfferingPK;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.sessions.AbstractFacade;
import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.visitors.ProvideOfferingFacadeVisitor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 *
 * @author jsie
 */
public abstract class AbstractBillService<T extends Bill> extends AbstractCommonService<T, BillPK> {

    @Inject
    ProfessionalFacade professionalFacade;

    @Inject
    ProvideOfferingFacadeVisitor visitor;

    private static final Logger LOG = Logger.getLogger(
            AbstractBillService.class.
                    getName());

    public AbstractBillService(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    public Response post(String prefix,
            Consumer<T> prepareAction,
            SecurityContext sec, T entity,
            String professional) {

        String proEmail = this.getProEmail(sec, professional);

        final long count = getFacade().count();
        getFacade().flush();

        return super.<Professional, String>post(entity, proEmail,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade,
                T::setProfessional,
                Professional::getBills, e -> {
            /**
             * Keep in mind that e is already added to the Professional Bills
             * list.
             *
             */
            if (e.getDeliveryDate() == null) {
                throw new WebApplicationException(
                        "A delivery date must be provided !");
            }

            this.setBillReference(e, prefix, count);

            this.checkPayment(e);

            List<PurchasedOffering> purchasedOfferings = e.
                    getPurchasedOfferings().
                    stream()
                    .map(po -> Optional.ofNullable(po.getOffering().accept(
                            visitor).find(new OfferingPK(
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

            prepareAction.accept(e);
        });
    }

    private void checkPayment(T bill) {

        if (bill.getPaymentDate() != null && bill.getPaymentDate().before(bill.
                getDeliveryDate())) {
            throw new WebApplicationException("Payment date " + bill.
                    getPaymentDate() + " cannot be before Delivery Date "
                    + bill.getDeliveryDate());
        }

        if (!bill.getPayments().isEmpty()) {
            if (bill.getPaymentDate() != null) {
                //Total amount should be equal to the sum of the amount's payment
                int total = bill.getPayments().stream()
                        .mapToInt(Payment::getAmount)
                        .sum();
                if (total != bill.getAmount()) {
                    String errmsg = "Amount is " + bill.getAmount()
                            + " "
                            + bill.getCurrency()
                            + " but the total amount computed (based on the payments) is "
                            + total
                            + " "
                            + bill.getCurrency();
                    LOG.log(Level.WARNING, errmsg);
                    throw new WebApplicationException(errmsg);
                }
            } else {
                LOG.log(Level.INFO,
                        "{2} {0} delivered on {1} has not been paid : payments recorded but no payment date provided yet !",
                        new Object[]{bill.getReference(), bill.
                            getDeliveryDate(), bill.getClass().getSimpleName()});
            }
        } else {
            if (bill.getPaymentDate() != null) {
                throw new WebApplicationException(
                        "A payment date is provided but there is no payment yet !");
            } else {
                LOG.log(Level.INFO,
                        "{2} {0} delivered on {1} has not been paid : no payment provided during the Bill and no payment date provided yet !",
                        new Object[]{bill.getReference(), bill.
                            getDeliveryDate(), bill.getClass().getSimpleName()});
            }
        }
    }

    public Response put(
            SecurityContext sec, T entity,
            String professional) {

        BillPK pk = new BillPK(entity.getReference(), entity.getDeliveryDate(),
                this.getProEmail(sec,
                        professional));

        LOG.log(Level.INFO, "Updating {1} {0}", new Object[]{getFacade().
            prettyPrintPK(pk), entity.getClass().getSimpleName()});
        return super.put(entity, pk, e -> {
            e.setComments(entity.getComments());

            if (e.getPaymentDate() == null) {
                checkPayment(entity);
                e.setPaymentDate(entity.getPaymentDate());
                e.setPayments(entity.getPayments());
            }
        });
    }

    public Response delete(
            Class<T> entityClass,
            Function<T, Boolean> deleteAction,
            String reference,
            long deliverydate,
            String professional) {

        BillPK pk = new BillPK(reference, new Date(deliverydate), professional);

        LOG.log(Level.INFO, "Deleting {1} {0}",
                new Object[]{getFacade().prettyPrintPK(pk),
                    entityClass.getSimpleName()});
        return super.delete(pk, e -> {
            e.getProfessional().getBills().remove(e);
            deleteAction.apply(e);
        });
    }

    private void setBillReference(T e, String prefix, long count) {
        e.setReference(prefix + "-" + e.getProfessional().getUuid() + "-"
                + new SimpleDateFormat("yyyyMMddHHmmss").format(e.
                        getDeliveryDate()) + "-" + e.hashCode());

    }
}
