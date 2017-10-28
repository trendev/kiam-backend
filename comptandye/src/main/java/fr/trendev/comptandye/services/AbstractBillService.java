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
import fr.trendev.comptandye.utils.visitors.BillTypeVisitor;
import fr.trendev.comptandye.utils.visitors.ProvideOfferingFacadeVisitor;
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
 * Create or update a Bill is more specific than the other object and requires
 * additional operations. This class is used to perform those operations/checks.
 *
 * @author jsie
 */
public abstract class AbstractBillService<T extends Bill> extends AbstractCommonService<T, BillPK> {

    @Inject
    ProfessionalFacade professionalFacade;

    /**
     * Used to gather a ServiceFacade or a PackFacade from an Offering.
     *
     */
    @Inject
    ProvideOfferingFacadeVisitor provideOfferingFacadeVisitor;

    /**
     * Used to gather a Bill prefix from a Bill type.
     *
     */
    @Inject
    BillTypeVisitor billTypeVisitor;

    private final Logger LOG = Logger.getLogger(
            AbstractBillService.class.
                    getName());

    public AbstractBillService(Class<T> entityClass) {
        super(entityClass);
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    /**
     * Prepares and persists a Bill.
     *
     * Before calling
     * {@link AbstractCommonService#post(java.lang.Object, java.lang.Object, java.util.function.BiFunction, java.lang.Class, fr.trendev.comptandye.sessions.AbstractFacade, java.util.function.BiConsumer, java.util.function.Function, java.util.function.Consumer)},
     * this method will:
     * <ul>
     * <li>check if a deliveryDate is provided (mandatory), </li>
     * <li>set the Bill's reference </li>
     * <li>and check the total amount from the payments amounts, the discount
     * and the purchased offering prices</li>
     * </ul>
     *
     * Before performing the prepareAction, the entity is added to the
     * Professional's bills list! A bill is owned by a Professional. Its email
     * (primary key) can be provided as a QueryParam or pulled up from the
     * Security Context.
     *
     * @param prepareAction operations to performed before persisting the entity
     * @param sec the injected security context
     * @param entity the json deserialized Bill
     * @param professional the professional's email, owner of the Bill (can be
     * null if Principal of Security Context is used).
     * @return the json serialized persisted Bill
     * @see AbstractCommonService#post(java.lang.Object, java.lang.Object,
     * java.util.function.BiFunction, java.lang.Class,
     * fr.trendev.comptandye.sessions.AbstractFacade,
     * java.util.function.BiConsumer, java.util.function.Function,
     * java.util.function.Consumer)
     */
    public Response post(Consumer<T> prepareAction,
            SecurityContext sec, T entity,
            String professional) {

        String proEmail = this.getProEmail(sec, professional);

        return super.<Professional, String>post(entity, proEmail,
                AbstractFacade::prettyPrintPK,
                Professional.class,
                professionalFacade,
                T::setProfessional,
                Professional::getBills, e -> {

            if (e.getDeliveryDate() == null) {
                throw new WebApplicationException(
                        "A delivery date must be provided !");
            }

            setBillReference(e, billTypeVisitor);

            this.checkPayment(e);

            if (e.getPurchasedOfferings() == null || e.getPurchasedOfferings().
                    isEmpty()) {
                throw new WebApplicationException(
                        "PurchasedOffering(s) must be provided !");
            }

            List<PurchasedOffering> purchasedOfferings = e.
                    getPurchasedOfferings().
                    stream()
                    .map(po -> Optional.ofNullable(po.getOffering().accept(
                            provideOfferingFacadeVisitor).find(new OfferingPK(
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

    /**
     * Checks the payments provided during the Bill creation. This method is
     * used during a POST and a PUT. The paymentDate cannot be before the
     * deliveryDate.
     *
     * @param bill the Bill to check
     * @throws WebApplicationException if :
     * <ul>
     * <li>paymentDate is before the deliveryDate</li>
     * <li>payments are empty and deliveryDate is provided</li>
     * <li>payments are provided but deliveryDate is not provided (null)</li>
     * </ul>
     */
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

    /**
     * Updates a Bill. The composed primary key is based on the Bill's
     * reference, its deliveryData and the owner's email extracted from the
     * SecurityContext or provided in a QueryParam. If a paymentDate is provided
     * to an unpaid (Bill without a payment date), a check of the dates
     * (deliveryDate/paymentDate) and the amounts will be performed and if
     * successful the Bill will be paid (paymentDate set and futur modification
     * will be ignored).
     *
     * @param sec the security context
     * @param entity the entity to update
     * @param professional the owner's email of the Bill
     * @return the updated Bill
     * @see AbstractCommonService#put(java.lang.Object, java.lang.Object,
     * java.util.function.Consumer)
     */
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

    /**
     * Finds a Bill and removes it from its owner (Professional) and authors
     * (Client, CollectiveGroup and Individual bills list).
     *
     * @param entityClass the Bill's class, used for logging purposes
     * @param deleteAction the actions to perform before deletion
     * @param reference the Bill's reference
     * @param deliverydate the Bill's deliveryDate
     * @param professional the owner's email of the Bill.
     * @return HTTP OK if deletion is done without exceptions
     */
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

    /**
     * Sets the the Bill's reference.
     *
     * [CX|CG|IX]-[PRO-UUID]-[deliveryDate: yyyyMMddHHmmss]-[hashcode]
     *
     * @param <T> the Bill's type ClientBill or CollectiveGroupBill or
     * IndividualBill
     * @param e the Bill to create
     * @param visitor a {@link BillTypeVisitor} visitor which will provide the
     * prefix from the Bill's type.
     */
    public static <T extends Bill> void setBillReference(T e,
            BillTypeVisitor visitor) {
        e.setReference(e.accept(visitor) + "-" + e.getProfessional().
                getUuid() + "-"
                + new SimpleDateFormat("yyyyMMddHHmmss").format(e.
                        getDeliveryDate()) + "-" + e.hashCode());
    }
}
