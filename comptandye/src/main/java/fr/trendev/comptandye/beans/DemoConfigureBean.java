/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Business;
import fr.trendev.comptandye.entities.Client;
import fr.trendev.comptandye.entities.CollectiveGroup;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.Expense;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.SocialNetworkAccounts;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.PasswordGenerator;
import fr.trendev.comptandye.utils.UUIDGenerator;
import fr.trendev.comptandye.utils.UserAccountType;
import fr.trendev.comptandye.utils.visitors.BillTypeVisitor;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author jsie
 */
@Singleton // javax.ejb.Singleton;
@Startup //javax.ejb.Startup
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
        trendevfr.setCltype(UserAccountType.ADMINISTRATOR);

        Administrator comptandye = new Administrator("comptandye@gmail.com",
                "mZWR4R0bp5EPs9xfOwUPu3n/06LOL+wHK6BuUBsHgQM=",
                "comptandye_admin", UUIDGenerator.generate("ADMIN-", true));
        comptandye.setCltype(UserAccountType.ADMINISTRATOR);

        Administrator jsie = new Administrator("julien.sie@gmail.com",
                "RrYJsV8xV7fsJkzgrFqGwiZzvIGEFan6e0ANYPcJhrI=", "jsie",
                UUIDGenerator.generate("ADMIN-", true));
        jsie.setCltype(UserAccountType.ADMINISTRATOR);

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
        vanessa.setCltype(UserAccountType.PROFESSIONAL);
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
        vanessa.setCompanyName("VANESSA ESTHETIQUE ET COIFFURE");
        cal.clear();
        cal.set(2007, 11, 15); // src infogreffe
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
                new PaymentMode("Chèque"),
                new PaymentMode("Espèces"),
                new PaymentMode("Virement")
        ));

        Professional audrey = new Professional("audreyheitzmann@gmail.com",
                "VvCdf6nrd7ksdB3RlAnAFSuhmyLgzeO/oeZpcPju7Fc=",
                "Audrey", UUIDGenerator.generate("PRO-", true));
        audrey.setCltype(UserAccountType.PROFESSIONAL);

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
        skonx.setCltype(UserAccountType.INDIVIDUAL);
        skonx.setPassword(PasswordGenerator.encrypt_SHA256(PasswordGenerator.
                autoGenerate()));

        Individual sylvioc = new Individual();
        sylvioc.setEmail("sylvie.gay@gmail.com");
        sylvioc.setCltype(UserAccountType.INDIVIDUAL);
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

}
