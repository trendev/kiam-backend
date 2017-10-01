/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Business;
import fr.trendev.comptandye.entities.Category;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.ClientBill;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.CollectiveGroupBill;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.IndividualBill;
import fr.trendev.comptandye.entities.Pack;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.PurchasedOffering;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.visitors.BillTypeVisitor;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UUIDGenerator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jsie
 */
@Singleton
@Startup
public class DemoConfigureBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(
            DemoConfigureBean.class.
                    getName());

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    @Inject
    UserGroupFacade userGroupFacade;

    @PostConstruct
    public void init() {
        this.initPaymentModes();
        this.initBusinesses();

        //check that the User Group table is empty
        if (userGroupFacade.findAll().isEmpty()) {
            this.initUsersAndGroups();
        }

        this.createCategoryAndClient();
        this.createBillsAndOfferings();
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

        em.persist(adminGroup);
        /**
         * Creates the first professional
         */
        Professional vanessa = new Professional("vanessa.gay@gmail.com",
                "EUrVrX4nfmYYFxpMyRX93OlkJxNZv9mkMGfirZKbhWI=", "Vaness",
                "PRO-825abefc0bb54b12808032ef6695655b");
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

        vanessa.setCompanyID("501476154");
        vanessa.setCompanyName("VANESSA ESTHETIQUE ET COIFFURE");
        cal.clear();
        cal.set(2014, 1, 20);
        vanessa.setCreationDate(cal.getTime());

        vanessa.getBusinesses().addAll(Arrays.asList(em.find(Business.class,
                "Esthétique"),
                em.find(Business.class, "Coiffure")));

        vanessa.getExpenses().add(new Expense("Material", 100000,
                new Date(), "invoice#1", Arrays.
                        asList("Partner", "Provider"), vanessa, Arrays.asList(
                new Payment(30000, em.find(PaymentMode.class, "CB")),
                new Payment(70000, em.find(PaymentMode.class, "Espèces"))),
                Arrays.asList(em.find(Business.class, "Coiffure"))));

        vanessa.getCollectiveGroups().add(
                new CollectiveGroup("Senior Residence", "0123456789",
                        new Address(
                                "10 route de la ferme du pavillon", "appart 202",
                                "77600", "Chanteloup-en-Brie"), vanessa));

        vanessa.getPaymentModes().addAll(Arrays.asList(
                new PaymentMode("CB"),
                new PaymentMode("Chèque"),
                new PaymentMode("Espèces"),
                new PaymentMode("Virement")
        ));

        /**
         * Creates the Professional user group
         */
        UserGroup pro = new UserGroup("Professional", "Professional User Group");

        /**
         * Adds the first user to the professional group and vice versa
         */
        vanessa.getUserGroups().add(pro);
        pro.getUserAccounts().add(vanessa);

        /**
         * Creates the Individual group, empty on the current version of the
         * application
         *
         */
        UserGroup ind = new UserGroup("Individual", "Individual User Group");

        Individual skonx = new Individual();
        skonx.setEmail("skonx2006@gmail.com");

        Individual sylvioc = new Individual();
        sylvioc.setEmail("sylvie.gay@gmail.com");

        List<Individual> individuals = IntStream
                .range(0, 10)
                .mapToObj(i -> new Individual("hank.moody-" + (i + 1)
                        + "@hella.com",
                        PasswordGenerator.encrypt_SHA256("Californication" + i),
                        "hankmoody_" + (i + 1), UUIDGenerator.
                                generate("IND-", true)))
                .collect(Collectors.toList());

        ind.getUserAccounts().add(skonx);
        skonx.getUserGroups().add(ind);

        ind.getUserAccounts().add(sylvioc);
        sylvioc.getUserGroups().add(ind);

        individuals.forEach(i -> {
            ind.getUserAccounts().add(i);
            i.getUserGroups().add(ind);
        });

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
            LOG.log(Level.INFO, "Id of Skonx Address = " + skonxAddress.
                    getId());
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

    private void createBillsAndOfferings() {
        try {
            Professional vanessa = em.find(Professional.class,
                    "vanessa.gay@gmail.com");
            Individual sylvioc = em.find(Individual.class,
                    "sylvie.gay@gmail.com");
            LOG.log(Level.WARNING, "Creating a bill for {0} / {1} / {2}",
                    new Object[]{vanessa.
                                getEmail(), vanessa.getUsername(), vanessa.
                        getUuid()});

            Business coiffure = em.find(Business.class, "Coiffure");
            Business esthetique = em.find(Business.class, "Esthétique");
            Service service1 = new Service("Fashion color", 5000, 60, vanessa);
            service1.setCltype("service");
            service1.getBusinesses().add(coiffure);

            Service service2 = new Service("Exclusive service for dark skin",
                    5000, 60,
                    vanessa);
            service2.setCltype("service");
            service2.getBusinesses().add(esthetique);

            vanessa.getOfferings().add(service1);
            vanessa.getOfferings().add(service2);
            Pack specialPack = new Pack("Supreme Pack", 8000, 120, vanessa);
            specialPack.setCltype("pack");
            specialPack.getOfferings().add(service1);
            specialPack.getOfferings().add(service2);
            specialPack.getBusinesses().add(coiffure);
            specialPack.getBusinesses().add(esthetique);
            vanessa.getOfferings().add(specialPack);

            Date deliveryDate = new Date();

            IndividualBill bill1 = new IndividualBill(null,
                    deliveryDate,
                    5000, 0,
                    new Date(),
                    Arrays.asList("Cool", "sympa"),
                    vanessa, new LinkedList<>(), Arrays.asList(
                            new PurchasedOffering(1, service1)),
                    sylvioc);

            bill1.setCltype(BillTypeVisitor.INDIVIDUAL_CLTYPE);
            bill1.setReference(BillTypeVisitor.INDIVIDUAL_PREFIX + "-" + vanessa.
                    getUuid()
                    + "-1-" + bill1.hashCode());

//            deliveryDate = new Date(System.currentTimeMillis() + 1);
            IndividualBill bill2 = new IndividualBill(null,
                    deliveryDate,
                    8000, 0,
                    new Date(),
                    Arrays.asList("Long", "Pffff"),
                    vanessa, new LinkedList<>(), Arrays.asList(
                            new PurchasedOffering(1, specialPack)),
                    sylvioc);
            bill2.setCltype(BillTypeVisitor.INDIVIDUAL_CLTYPE);
            bill2.setReference(BillTypeVisitor.INDIVIDUAL_PREFIX + "-" + vanessa.
                    getUuid()
                    + "-2-" + bill2.hashCode());

            sylvioc.getIndividualBills().add(bill1);
            sylvioc.getIndividualBills().add(bill2);

            Payment pm = new Payment(5000, em.find(PaymentMode.class,
                    "CB"));
            Payment pm2 = new Payment(8000, em.find(PaymentMode.class,
                    "Espèces"));
            bill1.getPayments().add(pm);
            vanessa.getBills().add(bill1);
            bill2.getPayments().add(pm2);
            vanessa.getBills().add(bill2);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error in createBills()", ex);
        }
    }

    private void createCategoryAndClient() {
        Professional vanessa = em.find(Professional.class,
                "vanessa.gay@gmail.com");
        Category cat1 = new Category("long time customers", "Fidelity", vanessa);
        Client client1 = new Client("valery.lamome@hotmail.fr", vanessa);

        client1.setAddress(new Address("down town", "water recycling",
                "77600", "Quincy-Voisins"));
        cat1.getClients().add(client1);
        client1.getCategories().add(cat1);

        vanessa.getClients().add(client1);
        client1.setProfessional(vanessa);

        client1.getCollectiveGroups().add(vanessa.getCollectiveGroups().get(0));
        vanessa.getCollectiveGroups().get(0).getClients().add(client1);

        vanessa.getCategories().add(cat1);

        Payment payment = new Payment(1500, em.find(PaymentMode.class,
                "Virement"));

        Service service = new Service("Classic Haircut", 1500, 60,
                vanessa);
        service.setCltype("service");
        Service service2 = new Service("A service for " + vanessa.
                getCollectiveGroups().get(0).getGroupName(), 3000, 120,
                vanessa);
        service2.setCltype("service");

        em.persist(service);
        em.persist(service2);

        ClientBill bill = new ClientBill(null,
                new Date(1506070013419l),
                1500, 0,
                new Date(), Arrays.asList("Has left her first son"), vanessa,
                Arrays.asList(payment), Arrays.asList(new PurchasedOffering(1,
                service)), client1);
        bill.setCltype(BillTypeVisitor.CLIENT_CLTYPE);
        bill.setReference(BillTypeVisitor.CLIENT_PREFIX + "-" + vanessa.
                getUuid()
                + "-1-" + bill.hashCode());

        vanessa.getBills().add(bill);
        client1.getClientBills().add(bill);

        CollectiveGroupBill cgbill = new CollectiveGroupBill("CG-" + vanessa.
                getUuid() + "-1", new Date(),
                3000, 0,
                new Date(), Arrays.asList("BAU", "Easy but long"), vanessa,
                Arrays.asList(new Payment(3000, em.
                        find(PaymentMode.class,
                                "Virement"))), Arrays.asList(
                new PurchasedOffering(1, service2)), vanessa.
                        getCollectiveGroups().get(0));
        cgbill.setCltype(BillTypeVisitor.COLLECTIVEGROUP_CLTYPE);
        cgbill.setReference(BillTypeVisitor.COLLECTIVEGROUP_PREFIX + "-" + vanessa.
                getUuid()
                + "-1-" + cgbill.hashCode());

        vanessa.getBills().add(cgbill);
        vanessa.getCollectiveGroups().get(0).getCollectiveGroupBills().add(
                cgbill);
    }

}
