/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Bill;
import fr.trendev.comptandye.entities.Business;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.ExpensePK;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Product;
import fr.trendev.comptandye.entities.ProductPK;
import fr.trendev.comptandye.entities.ProductReference;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Purchase;
import fr.trendev.comptandye.entities.PurchasedItem;
import fr.trendev.comptandye.entities.ReturnedItem;
import fr.trendev.comptandye.entities.Sale;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.SoldItem;
import fr.trendev.comptandye.entities.UsedItem;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.entities.VatRates;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UUIDGenerator;
import fr.trendev.comptandye.utils.visitors.BillTypeVisitor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
//@Singleton // javax.ejb.Singleton;
//@Startup //javax.ejb.Startup
public class DemoConfigureBean implements Serializable {

    private final Logger LOG = Logger.getLogger(
            DemoConfigureBean.class.
                    getName());

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    @Inject
    UserGroupFacade userGroupFacade;

    @Inject
    BillTypeVisitor billTypeVisitor;

    @PostConstruct
    public void init() {

        //check that the User Group table is empty
        if (userGroupFacade.findAll().isEmpty()) {
            this.initPaymentModes();
            this.initBusinesses();
            this.initUsersAndGroups();
            this.importClients();
            this.createVatRatesFR();
            this.createProductReference();
            this.createProduct();
            this.createSale();
            this.createProductRecords();
        }

    }

    private void initUsersAndGroups() {

        /**
         * Creates 3 administrators with different passwords
         */
//        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.YEAR, -2);
//        trendevfr.setRegistrationDate(cal.getTime());
        Administrator trendevfr = new Administrator("trendevfr@gmail.com",
                "ts15qkBmihdtvmkKXPgVmbPGeyQU6aKd5XNd5HwOzu0=",
                "trendevfr_admin", UUIDGenerator.generate("ADMIN-", true));

        Administrator comptandye = new Administrator("comptandye@gmail.com",
                "mZWR4R0bp5EPs9xfOwUPu3n/06LOL+wHK6BuUBsHgQM=",
                "comptandye_admin", UUIDGenerator.generate("ADMIN-", true));

        Administrator jsie = new Administrator("julien.sie@gmail.com",
                "RrYJsV8xV7fsJkzgrFqGwiZzvIGEFan6e0ANYPcJhrI=", "jsie",
                UUIDGenerator.generate("ADMIN-", true));

        /**
         * Creates the Administrator Group
         */
        UserGroup adminGroup = new UserGroup("Administrator",
                "Administrator Group");

        /**
         * Adds the administrators to the group and the group to the
         * administrators
         */
        Collections.addAll(adminGroup.getUserAccounts(), trendevfr, comptandye,
                jsie);
        trendevfr.getUserGroups().add(adminGroup);
        comptandye.getUserGroups().add(adminGroup);
        jsie.getUserGroups().add(adminGroup);
        trendevfr.setBlocked(false);
        comptandye.setBlocked(false);
        jsie.setBlocked(false);

        em.persist(adminGroup);
        /**
         * Creates the first professional
         */
        Professional vanessa = new Professional("vanessa.gay@gmail.com",
                "EUrVrX4nfmYYFxpMyRX93OlkJxNZv9mkMGfirZKbhWI=", "Vaness",
                UUIDGenerator.generate("PRO-", true));
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(1983, 9, 25);
        vanessa.setCustomerDetails(new CustomerDetails("Vanessa", "Gay",
                "Vaness", "0675295422", cal.getTime(), 'F', null, Arrays.
                asList(
                        "Fun", "Pro", "Living with a nice guy", "3 children")));

        vanessa.setAddress(new Address("47 Rue René Benoist", null, "77860",
                "Quincy-Voisins"));

        vanessa.setSocialNetworkAccounts(new SocialNetworkAccounts(
                "https://www.facebook.com/gayvanessa",
                "@VanessCE", null, "https://www.pinterest.com/vanessagay14/"));

        vanessa.setCompanyID("50147615400023");
        vanessa.setVatcode("FR87501476154");
        vanessa.setCompanyName("VANESSA ESTHETIQUE ET COIFFURE");
        cal.clear();
        cal.set(2007, 11, 15); // src infogreffe
        vanessa.setCreationDate(cal.getTime());

        vanessa.getBusinesses().addAll(Arrays.asList(em.find(Business.class,
                "Esthétique"),
                em.find(Business.class, "Coiffure")));

        vanessa.getExpenses().add(new Expense("Material", 100000,
                new Date(), Arrays.
                        asList("Material", "Haircut"), vanessa, Arrays.asList(
                new Payment(30000, em.find(PaymentMode.class, "CB")),
                new Payment(70000, em.find(PaymentMode.class, "Espèces"))),
                Arrays.asList(em.find(Business.class, "Coiffure"))));

        Purchase purchase = new Purchase();
        purchase.setDescription("Buy 4x color sticks");
        purchase.setAmount(1200);
        purchase.setPaymentDate(new Date());
        purchase.setProvider("BLEU LIBELLULE");
        purchase.setCategories(Arrays.asList("color"));
        purchase.setPayments(Arrays.asList(
                new Payment(1200, em.find(PaymentMode.class, "CB"))));
        purchase.setBusinesses(Arrays.
                asList(em.find(Business.class, "Coiffure")));
        purchase.setInvoiceRef("facture #2341");

        purchase.setProfessional(vanessa);
        vanessa.getExpenses().add(purchase);

        vanessa.getCollectiveGroups().add(
                new CollectiveGroup("Senior Residence", "0123456789",
                        new Address(
                                "10 route de la ferme du pavillon", "appart 202",
                                "77600", "Chanteloup-en-Brie"), vanessa));

        vanessa.getPaymentModes().addAll(Arrays.asList(
                new PaymentMode("Chèque"),
                new PaymentMode("Espèces"),
                new PaymentMode("Virement")
        ));

        Professional audrey = new Professional("audreyheitzmann@gmail.com",
                "VvCdf6nrd7ksdB3RlAnAFSuhmyLgzeO/oeZpcPju7Fc=",
                "Audrey", UUIDGenerator.generate("PRO-", true));

        audrey.setCustomerDetails(new CustomerDetails("Audrey", "Heitzmann",
                "Audrey", "0625719477", null, 'F', null, Collections.
                        <String>emptyList()));
        audrey.setAddress(new Address("2 rue path", null, "77135",
                "Pontcarre"));
        audrey.setSocialNetworkAccounts(new SocialNetworkAccounts());
        audrey.setCompanyID("81979715000011");
        audrey.setCompanyName("La nouvelle hair");
        cal.clear();
        cal.set(2016, 3, 19);
        audrey.setCreationDate(cal.getTime());
        audrey.getBusinesses().add(em.find(Business.class, "Coiffure"));
        audrey.getPaymentModes().addAll(Arrays.asList(
                new PaymentMode("Espèces")
        ));

        /**
         * Creates the Professional user group
         */
        UserGroup pro = new UserGroup("Professional", "Professional User Group");

        /**
         * Adds the first user to the professional group and vice versa
         */
        vanessa.getUserGroups().add(pro);
        audrey.getUserGroups().add(pro);
        pro.getUserAccounts().add(vanessa);
        pro.getUserAccounts().add(audrey);
        vanessa.setBlocked(false);
        audrey.setBlocked(false);

        /**
         * Creates the Individual group, empty on the current version of the
         * application
         *
         */
        UserGroup ind = new UserGroup("Individual", "Individual User Group");

        Individual skonx = new Individual();
        skonx.setEmail("skonx2006@gmail.com");
        skonx.setPassword(PasswordGenerator.encrypt_SHA256(PasswordGenerator.
                autoGenerate()));

        Individual sylvioc = new Individual();
        sylvioc.setEmail("sylvie.gay@gmail.com");
        sylvioc.setPassword(PasswordGenerator.encrypt_SHA256(PasswordGenerator.
                autoGenerate()));

        ind.getUserAccounts().add(skonx);
        skonx.getUserGroups().add(ind);

        ind.getUserAccounts().add(sylvioc);
        sylvioc.getUserGroups().add(ind);

        skonx.setBlocked(false);
        sylvioc.setBlocked(false);

        vanessa.getIndividuals().add(sylvioc);
        sylvioc.getProfessionals().add(vanessa);

        /**
         * Store the professional and individual groups and their contents
         */
        em.persist(pro);
        em.persist(ind);

        Address skonxAddress = new Address("79 avenue de la jonchere",
                "Appartement A113",
                "77600", "Chanteloup-en-Brie");

        em.persist(skonxAddress);
        skonx.setAddress(skonxAddress);

        em.flush();
        if (em.contains(skonxAddress)) {
            em.refresh(skonxAddress);
        }

    }

    private void initPaymentModes() {
        Arrays.
                asList("CB", "Chèque", "Paylib", "Paypal",
                        "Espèces", "Virement").stream().forEach(pm -> {
                    em.persist(new PaymentMode(pm));
                });
    }

    private void initBusinesses() {
        Arrays.
                asList("Coiffure", "Esthétique", "Onglerie").stream().forEach(
                b -> {
            em.persist(new Business(b));
        });
    }

    private void importClients() {
        try {
            Professional vanessa = em.find(Professional.class,
                    "vanessa.gay@gmail.com");

            ClassLoader classloader = Thread.currentThread().
                    getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("csv/clients.csv");

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(is))) {

                in.lines().skip(1).forEach(l -> {
                    String[] fields = l.split(",", -1);
                    if (fields.length != 12) {
                        throw new WebApplicationException(
                                "Error during clients import : fields length shoud be 12");
                    }

                    Date bd = null;
                    try {
                        bd = sdf.parse(fields[2]);
                    } catch (ParseException ex) {
                    }

                    Client c = new Client(fields[7],
                            new SocialNetworkAccounts(fields[8], fields[9],
                                    fields[11], fields[10]),
                            new CustomerDetails(fields[1], fields[0], null,
                                    fields[3],
                                    bd,
                                    'F',
                                    null, Collections.<String>emptyList()),
                            new Address(fields[4], null, fields[5], fields[6]),
                            vanessa);
                    vanessa.getClients().add(c);
                });

            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Error during clients import",
                        ex);
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error in createBills()", ex);
        }

    }

    private void createVatRatesFR() {
        Professional vanessa = em.find(Professional.class,
                "vanessa.gay@gmail.com");

        VatRates vrFR = new VatRates();
        vrFR.setCountryId("FR");
        vrFR.setCountry(vanessa.getAddress().getCountry());
        vrFR.getRates().add(new BigDecimal("20"));
        vrFR.getRates().add(new BigDecimal("10"));
        vrFR.getRates().add(new BigDecimal("5.5"));
        vrFR.getRates().add(new BigDecimal("2.1"));

        em.persist(vrFR);

        vanessa.setVatRates(vrFR);
    }

    private void createProductReference() {
        ProductReference ref = new ProductReference();
        ref.setBarcode("1234567890");
        ref.setBrand("L'OREAL");
        ref.setDescription("Color stick - Purple");
        ref.setBusiness(em.find(Business.class, "Coiffure"));

        em.persist(ref);
    }

    private void createProduct() {

//        ProductReference ref = em.find(ProductReference.class, "1234567890");
        ProductReference ref = new ProductReference();
        ref.setBarcode("1234567891");
        ref.setBrand("L'OREAL");
        ref.setDescription("Color stick - Blond");
        ref.setBusiness(em.find(Business.class, "Coiffure"));

        Professional vanessa = em.find(Professional.class,
                "vanessa.gay@gmail.com");

        Product product = new Product();
        product.setThresholdWarning(2);
        product.setThresholdSevere(1);

        product.setProfessional(vanessa);
        product.setProductReference(ref);

        vanessa.setStock(Arrays.asList(product));
        em.persist(product);

    }

    private void createSale() {
        Sale sale = new Sale();
        sale.setName("2x Color stick - Blond - L'OREAL");
        sale.setShortname("2x Bld stick");
        sale.setPrice(1500);
        sale.setDuration(0);
        sale.setBusinesses(Arrays.asList(em.find(Business.class, "Coiffure")));
        sale.setQty(2);

        Professional vanessa = em.find(Professional.class,
                "vanessa.gay@gmail.com");

        vanessa.getOfferings().add(sale);
        sale.setProfessional(vanessa);

//        Product product = vanessa.getStock().get(0);
        Product product = em.find(Product.class,
                new ProductPK(vanessa.getEmail(), "1234567891"));

        sale.setProduct(product);
        product.getSales().add(sale);

        em.persist(sale);
    }

    private void createProductRecords() {
        PurchasedItem pr = new PurchasedItem();
        pr.setQty(4);

        Professional vanessa = em.find(Professional.class,
                "vanessa.gay@gmail.com");
        Purchase purchase = em.find(Purchase.class, new ExpensePK(vanessa.
                getExpenses().get(1).
                getId(), vanessa.getEmail()));

        pr.setPurchase(purchase);
        purchase.getPurchasedItems().add(pr);

        Product product = em.find(Product.class,
                new ProductPK(vanessa.getEmail(), "1234567891"));

        product.getTrackRecord().add(pr);
        pr.setProduct(product);
        product.setAvailableQty(product.getAvailableQty() + pr.getQty());

        UsedItem u = new UsedItem();
        u.setQty(1);

        product.getTrackRecord().add(u);
        u.setProduct(product);
        product.setAvailableQty(product.getAvailableQty() - u.getQty());

        Bill bill = new ClientBill();
        bill.setProfessional(vanessa);
        bill.setReference("CX-SOMETHING");
        bill.setDeliveryDate(new Date());
        bill.setAmount(4000);

        em.persist(bill);

        SoldItem si = new SoldItem();
        si.setQty(2);
        si.setBill(bill);

        product.getTrackRecord().add(si);
        si.setProduct(product);
        product.setAvailableQty(product.getAvailableQty() - si.getQty());

        bill.setCancelled(true);
        bill.setCancellationDate(new Date());

        ReturnedItem ri = new ReturnedItem();
        ri.setQty(si.getQty());
        ri.setCancelledBill(bill);

        product.getTrackRecord().add(ri);
        ri.setProduct(product);
        product.setAvailableQty(product.getAvailableQty() + ri.getQty());

        em.persist(pr);
        em.persist(u);
        em.persist(si);
        em.persist(ri);

    }

}
