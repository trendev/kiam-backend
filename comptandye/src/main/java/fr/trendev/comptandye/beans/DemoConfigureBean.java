/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Business;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.sessions.UserGroupFacade;
import fr.trendev.comptandye.utils.UUIDGenerator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOG = Logger.getLogger(DemoConfigureBean.class.
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

        this.displayUserGroupDetails();
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
        Professional vgay = new Professional();
        vgay.setEmail("vanessa.gay@gmail.com");
        vgay.setPassword("EUrVrX4nfmYYFxpMyRX93OlkJxNZv9mkMGfirZKbhWI=");
        vgay.setUsername("Vaness");
        vgay.setUuid(UUIDGenerator.generate("PRO_", true));

        /**
         * Creates the Professional user group
         */
        UserGroup pro = new UserGroup("Professional", "Professional User Group");

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
        UserGroup ind = new UserGroup("Individual", "Individual User Group");

        Individual skonx = new Individual();
        skonx.setEmail("skonx2006@gmail.com");

        Individual sylvioc = new Individual();
        sylvioc.setEmail("sylvie.gay@gmail.com");

        ind.getUserAccounts().add(skonx);
        skonx.getUserGroups().add(ind);

        ind.getUserAccounts().add(sylvioc);
        sylvioc.getUserGroups().add(ind);

        /**
         * Store the groups and their contents
         */
        em.persist(adminGroup);
        em.persist(pro);
        em.persist(ind);

        sylvioc.getUserGroups().forEach(g -> g.getUserAccounts().remove(
                sylvioc));

        em.remove(sylvioc);

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

}
