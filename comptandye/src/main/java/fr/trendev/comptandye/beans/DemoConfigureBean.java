/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.entities.Address;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Business;
import fr.trendev.comptandye.entities.CustomerDetails;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.IndividualBill;
import fr.trendev.comptandye.entities.Payment;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.Service;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
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
    
    private static final Logger logger = Logger.getLogger(
            DemoConfigureBean.class.
                    getName());
    
    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;
    
    @Inject
    UserGroupFacade userGroupFacade;
    
    @PostConstruct
    public void init() {
        this.clean();
        //check that the User Group table is empty
        if (userGroupFacade.findAll().isEmpty()) {
            this.initUsersAndGroups();
        }
        this.initPaymentModes();
        this.initBusinesses();

//        this.displayUserGroupDetails();
        this.createBills();
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
                "trendevfr_admin", UUIDGenerator.generate("ADMIN_", true));
        
        Administrator comptandye = new Administrator("comptandye@gmail.com",
                "mZWR4R0bp5EPs9xfOwUPu3n/06LOL+wHK6BuUBsHgQM=",
                "comptandye_admin", UUIDGenerator.generate("ADMIN_", true));
        
        Administrator jsie = new Administrator("julien.sie@gmail.com",
                "RrYJsV8xV7fsJkzgrFqGwiZzvIGEFan6e0ANYPcJhrI=", "jsie",
                UUIDGenerator.generate("ADMIN_", true));

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

        /**
         * Creates the first professional
         */
        Professional vanessa = new Professional("vanessa.gay@gmail.com",
                "EUrVrX4nfmYYFxpMyRX93OlkJxNZv9mkMGfirZKbhWI=", "Vaness",
                UUIDGenerator.generate("PRO_", true));
        Calendar cal = Calendar.getInstance();
        cal.set(1983, 9, 25, 0, 0, 0);
        vanessa.setCustomerDetails(new CustomerDetails("Vanessa", "Gay",
                "Vaness", "0675295422", cal.getTime(), 'F', "FS path", Arrays.
                asList(
                        "Fun", "Pro", "Living with a nice guy", "3 children")));

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
                                generate("IND_", true)))
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
         * Store the groups and their contents
         */
        em.persist(adminGroup);
        em.persist(pro);
        em.persist(ind);
        
        Address skonxAddress = new Address("79 avenue de la jonchere",
                "Appartement A113",
                "77600", "Chanteloup-en-Brie");
        
        em.persist(skonxAddress);
        skonx.setAddress(skonxAddress);
        
        em.merge(skonx);
        
        em.flush();
        if (em.contains(skonxAddress)) {
            em.refresh(skonxAddress);
            logger.log(Level.INFO, "Id of Skonx Address = " + skonxAddress.
                    getId());
        }
        
    }
    
    private void displayUserGroupDetails() {
        List<UserGroup> userGroup = userGroupFacade.findAll();
        userGroup.forEach(group -> {
            logger.info("## GROUP ##");
            logger.log(Level.INFO, "Name = {0}", group.getName());
            
            logger.log(Level.INFO, "Description = {0}", group.getDescription());
            
            int n = group.getUserAccounts().size();
            logger.
                    log(Level.INFO, "{0} User{1}", new Object[]{n,
                n > 1 ? "s" : ""});
            
            if (n > 0) {
                logger.info("Users id: ");
            }
            group.getUserAccounts().forEach(u -> logger.log(Level.INFO,
                    "- {0}",
                    u.
                            getEmail()));
            
            logger.info("###########");
        });
    }
    
    private void clean() {
        userGroupFacade.findAll().forEach(g -> em.remove(g));
        
        if (em.isJoinedToTransaction()) {
            try {
                em.flush();
                logger.log(Level.INFO, "clean() : OK");
            } catch (Exception e) {
                logger.log(Level.WARNING, "clean() : FAILED ==> {0}", e.
                        getMessage());
            }
        }
        logger.log(Level.INFO, "EntityManager is{0} joined to transaction", em.
                isJoinedToTransaction() ? "" : " not");
        logger.log(Level.INFO, "EntityManager is{0} opened",
                em.isOpen() ? "" : " not");
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
    
    private void createBills() {
        try {
            Professional vanessa = em.find(Professional.class,
                    "vanessa.gay@gmail.com");
            Individual sylvioc = em.find(Individual.class,
                    "sylvie.gay@gmail.com");
            logger.log(Level.WARNING, "Creating a bill for {0} / {1} / {2}",
                    new Object[]{vanessa.
                                getEmail(), vanessa.getUsername(), vanessa.
                        getUuid()});
            
            IndividualBill bill = new IndividualBill("Ref#12345", new Date(),
                    10000, 0,
                    new Date(),
                    Arrays.asList("Cool", "sympa"),
                    vanessa, new LinkedList<>(), Arrays.asList(new Service(
                            "un truc long", 10000, 60)), sylvioc);
            
            Payment pm = new Payment(10000, "EUR", em.find(PaymentMode.class,
                    "CB"));
            bill.getPayments().add(pm);
            vanessa.getBills().add(bill);
            em.merge(vanessa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
