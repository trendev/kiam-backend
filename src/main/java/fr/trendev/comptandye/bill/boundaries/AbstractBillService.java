/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.bill.boundaries;

import fr.trendev.comptandye.bill.controllers.BillFacade;
import fr.trendev.comptandye.bill.controllers.BillTypeVisitor;
import fr.trendev.comptandye.bill.entities.Bill;
import fr.trendev.comptandye.bill.entities.BillPK;
import fr.trendev.comptandye.common.boundaries.AbstractCommonService;
import fr.trendev.comptandye.common.controllers.AbstractFacade;
import fr.trendev.comptandye.exceptions.ExceptionHelper;
import fr.trendev.comptandye.exceptions.InvalidDeliveryDateException;
import fr.trendev.comptandye.offering.controllers.ProvideOfferingFacadeVisitor;
import fr.trendev.comptandye.offering.entities.DiscoverSalesVisitor;
import fr.trendev.comptandye.offering.entities.OfferingPK;
import fr.trendev.comptandye.payment.entities.Payment;
import fr.trendev.comptandye.product.controllers.ProductFacade;
import fr.trendev.comptandye.product.entities.Product;
import fr.trendev.comptandye.product.entities.ProductPK;
import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.professional.entities.Professional;
import fr.trendev.comptandye.purchasedoffering.entities.PurchasedOffering;
import fr.trendev.comptandye.sale.entities.Sale;
import fr.trendev.comptandye.solditem.entities.SoldItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ejb.EJBTransactionRolledbackException;
import javax.inject.Inject;
import javax.json.Json;
import javax.persistence.RollbackException;
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
 * Create or update a Bill is more specific than the other object and requires
 * additional operations.This class is used to perform those operations/checks.
 *
 * @author jsie
 * @param <T> subtype of Bill
 */
public abstract class AbstractBillService<T extends Bill> extends AbstractCommonService<T, BillPK> {

    @Inject
    ProfessionalFacade professionalFacade;

    /**
     * Used to gather a ServiceFacade or a PackFacade or a SaleFacade from an
     * Offering.
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

    @Inject
    private DiscoverSalesVisitor discoverSalesVisitor;

    @Inject
    private ProductFacade productFacade;

    @Inject
    private BillFacade billFacade;

//    @Inject
//    private SoldItemFacade soldItemFacade;
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

                    if (e.getProfessional().getBillsRefDate() == null) {
                        //fix the bill reference date of the professional with the delivery date of the first bill
                        e.getProfessional().setBillsRefDate(e.getDeliveryDate());
                    } else {
                        //filter bills with a delivery date earlier to the ref
                        //prevent the user to create backdated bills
                        if (e.getDeliveryDate().before(e.getProfessional().
                                getBillsRefDate())) {
                            throw new InvalidDeliveryDateException(
                                    String.valueOf(
                                            e.getProfessional().getBillsRefDate().
                                                    getTime()),
                                    Response.Status.CONFLICT);
                        } else {
                            //set the ref with the current bill
                            e.getProfessional().setBillsRefDate(e.getDeliveryDate());
                        }
                    }

                    //increment the bill count, will be used in concurrency context
                    e.getProfessional().setBillsCount(e.getProfessional().
                            getBillsCount() + 1);

                    setBillReference(e, billTypeVisitor);

                    //a new bill can not be cancelled yet
                    e.setCancelled(false);
                    e.setCancellationDate(null);

                    // check if the professional has a VAT code before submitting a bill with VAT rates
                    if (e.isVatInclusive() && e.getProfessional().getVatcode() == null) {
                        throw new WebApplicationException(
                                "Impossible to apply VAT on Bill without a VAT Code !");
                    }

                    // control the payments (if provided)
                    this.controlPayment(e);

                    if (e.getPurchasedOfferings() == null || e.getPurchasedOfferings().
                    isEmpty()) {
                        throw new WebApplicationException(
                                "PurchasedOffering(s) must be provided !");
                    }

                    // Rebuild a purchased offering list from the provided one
                    List<PurchasedOffering> purchasedOfferings = e.
                            getPurchasedOfferings().
                            stream()
                            // control the Offering for each PurchasedOffering
                            .map(_po -> Optional.ofNullable(_po.getOffering().accept( // get the offering Facade
                            provideOfferingFacadeVisitor).find(new OfferingPK(
                                    _po.getOffering().getId(),
                                    e.getProfessional().getEmail())))
                            .map(o -> {
                                //create a new PurchasedOffering from the Offering previously found
                                PurchasedOffering po = new PurchasedOffering(
                                        _po.getQty(), o);

                                // set the purchased offering id (security reason)
                                po.setId(UUIDGenerator.generateID());

                                // update the VAT rate if bill is VAT inclusive
                                if (e.isVatInclusive()) {
                                    po.setVatRate(Optional.ofNullable(_po.
                                            getVatRate()).map(Function.
                                                    identity())
                                            .orElseThrow(()
                                                    -> new WebApplicationException(
                                                    "VAT Rate missing for Offering "
                                                    + o.getName()))
                                    );
                                }

                                // update the stock if the bill includes sales
                                this.updateProduct(
                                        o.accept(discoverSalesVisitor),
                                        po.getQty(), e);

                                //link the Offering with the PurchasedOffering
                                o.getPurchasedOfferings().add(po);
                                return po;
                            })
                            .orElseThrow(()
                                    -> new WebApplicationException(
                                    _po.getOffering().getClass().
                                            getSimpleName()
                                    + " " + _po.getOffering().getId()
                                    + " does not exist !"))
                            ).collect(Collectors.toList());

                    //compute the total amount from the offerings prices and quantities
                    int total = purchasedOfferings.stream()
                            .mapToInt(po -> !e.isVatInclusive()
                            // without a VAT rate
                            ? po.getQty() * po.getOffering().getPrice()
                            // with a VAT rate
                            : po.getQty() * new BigDecimal(po.getOffering().
                            getPrice()).multiply(
                            po.getVatRate().add(
                                    new BigDecimal(100))).divide(new BigDecimal(
                                    100)).setScale(0,
                            RoundingMode.HALF_UP).intValue()
                            )
                            .sum();

                    if (e.getAmount() != (total - e.getDiscount())) {
                        String errmsg = "Amount is " + e.getAmount()
                        + " but the total amount computed (based on the purchased offerings prices and the discount) is "
                        + "(" + total + "-" + e.getDiscount() + ") = "
                        + (total - e.getDiscount());
                        LOG.log(Level.WARNING, errmsg);
                        throw new WebApplicationException(errmsg);
                    }

                    e.setPurchasedOfferings(purchasedOfferings);

                    //fix the issue date
                    e.setIssueDate(new Date());

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
    private void controlPayment(T bill) {

        // accept if paymentDate and deliveryDate are the same
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
                            + " but the total amount computed (based on the payments) is "
                            + total;
                    LOG.log(Level.WARNING, errmsg);
                    throw new WebApplicationException(errmsg);
                }

                // override the payments ids (security reason)
                bill.getPayments().forEach(p -> p.setId(UUIDGenerator.generateID()));
            } else {
                LOG.log(Level.INFO,
                        "{2} {0} delivered on {1} has not been paid : payments recorded but no payment date provided yet !",
                        new Object[]{bill.getReference(), bill.
                            getDeliveryDate(), bill.getClass().getSimpleName()});
            }
        } else {
            if (bill.getPaymentDate() != null) {
                if (bill.getAmount() <= 0) {
                    LOG.log(Level.INFO,
                            "{2} {0} delivered on {1} has no payments and might be closed: Free or Credit. Will check total and discount integrity later...",
                            new Object[]{bill.getReference(), bill.
                                getDeliveryDate(), bill.getClass().
                                        getSimpleName()});
                } else {
                    throw new WebApplicationException(
                            "A payment date is provided but there is no payment yet and Amount is positive !");
                }
            } else {
                LOG.log(Level.INFO,
                        "{2} {0} delivered on {1} has not been paid : no payment provided during the Bill and no payment date provided yet !",
                        new Object[]{bill.getReference(), bill.
                            getDeliveryDate(), bill.getClass().getSimpleName()});
            }
        }
    }

    /**
     * Updates a Bill. Controls the payments but cannot cancel a Bill. The
     * composed primary key is based on the Bill's reference, its deliveryData
     * and the owner's email extracted from the SecurityContext or provided in a
     * QueryParam. If a paymentDate is provided to an unpaid (Bill without a
     * payment date), a check of the dates (deliveryDate/paymentDate) and the
     * amounts will be performed and if successful the Bill will be paid
     * (paymentDate set and other modifications will be ignored). If a bill is
     * canceled, only comments can be updated.
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

            //just controls the payments
            if (!e.isCancelled() && e.getPaymentDate() == null) {
                this.controlPayment(entity);
                e.setPaymentDate(entity.getPaymentDate());
                e.setPayments(entity.getPayments());
            } // if the bill is cancelled or paid, do nothing

        });
    }

    /**
     * Finds a Bill and removes it from its owner (Professional) and authors
     * (Client, CollectiveGroup and Individual bills list). Professional's
     * BillsRefDate field is not updated when a Bill is deleted!
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
            //remove the bill from the professional bill list
            e.getProfessional().getBills().remove(e);

            //break the relation between the bill's purchasedoffering and the offering
            e.getPurchasedOfferings().forEach(po -> {
                //delete the relation if the purchasedoffering is linked with an active offering
                if (po.getOffering() != null) {
                    po.getOffering().getPurchasedOfferings().remove(po);
                    po.setOffering(null);
                }
                //otherwise, offering has already been deleted and offering should be null
            });
            deleteAction.apply(e);
        });
    }

    /**
     * Sets the the Bill's reference.[PRO-UUID]-[Bill Ref
     * Number]-[CX|CG|IX][deliveryDate: yyyyMMdd]
     *
     *
     * @param e the Bill to create
     * @param visitor a {@link BillTypeVisitor} visitor which will provide the
     * prefix from the Bill's type.
     */
    public void setBillReference(T e,
            BillTypeVisitor visitor) {
        e.setReference(e.getProfessional().getUuid() + "-"
                + e.getProfessional().getBillsCount() + "-"
                + e.accept(visitor)
                + new SimpleDateFormat("yyyyMMdd").format(e.
                        getDeliveryDate())
        );
    }

    /**
     * Controls the Products quantities and creates specific SoldItems.
     *
     * @param sales the sales in the purchased Offerings
     * @param poQty the purchased offering required quantity
     * @param bill the bill linked to the SoldItems
     */
    private void updateProduct(List<Sale> sales,
            int poQty,
            Bill bill) {
        sales.forEach(s -> {
            if (s.getProduct() == null) {
                throw new IllegalStateException("Product is NULL in Sale "
                        + s.getName());
            }

            ProductPK pk = new ProductPK(
                    s.getProfessional().getEmail(),
                    s.getProduct().getProductReference().getBarcode());
            Product product = productFacade.find(pk);

            // the required quantity
            int reqQty = s.getQty() * poQty;
            // the new product's quantity
            int qty = product.getAvailableQty() - reqQty;
            if (qty < 0) { // blocks the bill
                throw new WebApplicationException(
                        "Impossible to create bill because there is not enough Product "
                        + productFacade.prettyPrintPK(pk) + ". Required = "
                        + reqQty + " / Available = "
                        + product.getAvailableQty());
            } else {
                /**
                 * Update the available quantity. Alerts are automatically sent
                 * using the Product LifeCycle events.
                 */
                product.setAvailableQty(qty);

                /**
                 * Creates SoldItem automatically. Will be persist thanks to the
                 * CascadeType.ALL relationship between Product and
                 * ProductRecord.
                 */
                SoldItem si = new SoldItem();
                si.setId(UUIDGenerator.generateID());
                si.setQty(reqQty);
                si.setProduct(product);
                product.getProductRecords().add(si);
                si.setBill(bill);
            }

        });
    }

    /**
     * Cancels a Bill and updates the BillsRefDate attribute of the owner
     *
     * @param sec the security context
     * @param reference the bill's reference
     * @param deliverydate the bill's delivery date
     * @param professional the owner
     * @return 200 OK or throw a WebAppplicationException (417 not expected)
     */
    @Path("cancel/{reference}/{deliverydate}")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response cancel(@Context SecurityContext sec,
            @PathParam("reference") String reference,
            @PathParam("deliverydate") long deliverydate,
            @QueryParam("professional") String professional) {

        String proEmail = this.getProEmail(sec, professional);
        BillPK pk = new BillPK(reference, new Date(deliverydate),
                proEmail);

        LOG.log(Level.INFO, "REST request to cancel Bill : {0}",
                getFacade().
                        prettyPrintPK(
                                pk));
        try {
            return Optional.ofNullable(getFacade().find(pk))
                    .map(bill -> {
                        if (!bill.isCancelled()) {
                            bill.setCancelled(true);
                            bill.setCancellationDate(new Date());

                            resetBillsRefDate(bill.getProfessional());

                            getLogger().log(Level.INFO, "Bill "
                                    + " {0} cancelled", getFacade().
                                            prettyPrintPK(pk));
                        }// do nothing if the bill is already cancelled

                        return Response.ok().build();
                    })
                    .orElse(Response.status(Response.Status.NOT_FOUND).entity(
                            Json.createObjectBuilder().add("error",
                                    "Bill "
                                    + getFacade().prettyPrintPK(pk)
                                    + " not found").
                                    build()).
                            build());
        } catch (EJBTransactionRolledbackException | RollbackException ex) {
            throw ex;
        } catch (Exception ex) {

            String errmsg = ExceptionHelper.handleException(ex,
                    "Exception occurs cancelling Bill "
                    + getFacade().prettyPrintPK(pk));
            getLogger().log(Level.WARNING, errmsg, ex);
            throw new WebApplicationException(errmsg, ex);
        }
    }

    /**
     * Get the last valid Bills deliveryDate reference and update the owner.
     *
     * @param professional the professional to update
     * @throws IllegalStateException if there are too much valid dates found
     */
    private void resetBillsRefDate(Professional professional) {
        List<Date> dates = billFacade.
                findLastValidBillsRefDate(
                        professional);

        switch (dates.size()) {
            case 0:
                LOG.log(Level.INFO,
                        "All Bills of Professionnal {0} are cancelled! No deliveryDate limit",
                        professional.getEmail());
                professional.setBillsRefDate(
                        null);
                break;
            case 1:
                LOG.log(Level.INFO,
                        "DeliveryDate limit of Professionnal "
                        + professional.getEmail() + " reset to " + dates.
                        get(0));
                professional.setBillsRefDate(
                        dates.get(0));
                break;
            default:
                throw new IllegalStateException(
                        "Too much BillsRefDates found");
        }
    }
}
