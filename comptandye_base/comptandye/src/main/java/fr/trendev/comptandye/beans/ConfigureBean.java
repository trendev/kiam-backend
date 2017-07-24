/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.ejbsessions.UserGroupFacade;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.util.UUIDGenerator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author jsie
 */
@Singleton
@Startup
public class ConfigureBean implements Serializable {

    private static final Logger LOG = Logger.getLogger(ConfigureBean.class.
            getName());

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    @EJB
    UserGroupFacade userGroupFacade;

    @PostConstruct
    public void init() {
        this.clean();
        //check that the User Group table is empty
        if (userGroupFacade.findAll().isEmpty()) {
            this.initUsersAndGroups();
        }
        this.displayUserGroupDetails();
    }

    private void initUsersAndGroups() {

        /**
         * Creates 3 administrators with different passwords
         */
        Administrator trendevfr = new Administrator();
        trendevfr.setEmail("trendevfr@gmail.com");
        trendevfr.setPassword("ts15qkBmihdtvmkKXPgVmbPGeyQU6aKd5XNd5HwOzu0=");
        trendevfr.setUserGroups(new LinkedList<>());
        trendevfr.setUsername("trendevfr_admin");

        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.YEAR, -2);
        trendevfr.setRegistrationDate(cal.getTime());

        Administrator comptandye = new Administrator();
        comptandye.setEmail("comptandye@gmail.com");
        comptandye.setPassword("mZWR4R0bp5EPs9xfOwUPu3n/06LOL+wHK6BuUBsHgQM=");
        comptandye.setUserGroups(new LinkedList<>());
        comptandye.setUsername("comptandye_admin");

        comptandye.setRegistrationDate(cal.getTime());

        Administrator jsie = new Administrator();
        jsie.setEmail("julien.sie@gmail.com");
        jsie.setPassword("RrYJsV8xV7fsJkzgrFqGwiZzvIGEFan6e0ANYPcJhrI=");
        jsie.setUserGroups(new LinkedList<>());
        jsie.setUsername("jsie");

        jsie.setRegistrationDate(cal.getTime());

        /**
         * Creates the Administrator Group
         */
        UserGroup adminGroup = new UserGroup();
        adminGroup.setName("Administrator");
        adminGroup.setDescription("This is the Administrator Group");
        adminGroup.setUserAccounts(new LinkedList<>());

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
        Professional vgay = new Professional();
        vgay.setEmail("vanessa.gay@gmail.com");
        vgay.setPassword("EUrVrX4nfmYYFxpMyRX93OlkJxNZv9mkMGfirZKbhWI=");
        vgay.setUserGroups(new LinkedList<>());
        vgay.setUsername("Vaness");
        vgay.setUuid(UUIDGenerator.generate("PRO_", true));

        /**
         * Creates the Professional user group
         */
        UserGroup pro = new UserGroup();
        pro.setName("Professional");
        pro.setDescription("This is the Professional User Group");
        pro.setUserAccounts(new LinkedList<>());

        /**
         * Adds the first user to the professional group and vice versa
         */
        vgay.getUserGroups().add(pro);
        pro.getUserAccounts().add(vgay);

        /**
         * Creates the Individual group, empty on the current version of the
         * application
         *
         */
        UserGroup ind = new UserGroup();
        ind.setName("Individual");
        ind.setDescription("This is the Individual User Group");
        ind.setUserAccounts(Collections.EMPTY_LIST);

        /**
         * Store the groups and their contents
         */
        em.persist(adminGroup);
        em.persist(pro);
        em.persist(ind);

        this.initPaymentModes();
    }

    private void displayUserGroupDetails() {
        List<UserGroup> userGroup = userGroupFacade.findAll();
        userGroup.forEach(group -> {
            LOG.info("## GROUP ##");
            LOG.log(Level.INFO, "Name = {0}", group.getName());

            LOG.log(Level.INFO, "Description = {0}", group.getDescription());

            int n = group.getUserAccounts().size();
            LOG.
                    log(Level.INFO, "{0} User{1}", new Object[]{n,
                n > 1 ? "s" : ""});

            if (n > 0) {
                LOG.info("Users id: ");
            }
            group.getUserAccounts().forEach(u -> LOG.log(Level.INFO, "- {0}",
                    u.
                            getEmail()));

            LOG.info("###########");
        });
    }

    private void clean() {
        userGroupFacade.findAll().forEach(g -> em.remove(g));

        if (em.isJoinedToTransaction()) {
            try {
                em.flush();
                LOG.log(Level.INFO, "clean() : OK");
            } catch (Exception e) {
                LOG.log(Level.WARNING, "clean() : FAILED ==> {0}", e.
                        getMessage());
            }
        }
        LOG.log(Level.INFO, "EntityManager is{0} joined to transaction", em.
                isJoinedToTransaction() ? "" : " not");
        LOG.log(Level.INFO, "EntityManager is{0} opened",
                em.isOpen() ? "" : " not");
    }

    private void initPaymentModes() {
        Arrays.
                asList("CreditCard", "Cheque", "Paylib", "Paypal",
                        "Cash").stream().forEach(s -> {
                    PaymentMode pm = new PaymentMode();
                    pm.setName(s);
                    em.persist(pm);
                });
    }

}
