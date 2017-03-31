/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.ejbsessions.UserGroupFacade;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.PaymentMode;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.util.PasswordGenerator;
import fr.trendev.comptandye.util.UUIDGenerator;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
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
        clean();
        initUsersAndGroups();
        displayUserGroupDetails();
    }

    private void initUsersAndGroups() {

        Administrator admin = new Administrator();
        admin.setEmail("julien.sie@gmail.com");
        admin.setPassword(PasswordGenerator.encrypt_SHA256("password"));
        admin.setUserGroups(new LinkedList<>());
        admin.setUuid(UUIDGenerator.generate(true));
        admin.setUsername("admin");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        admin.setRegistrationDate(cal.getTime());

        Administrator csie = new Administrator();
        csie.setEmail("csie63@gmail.com");
        csie.setPassword(PasswordGenerator.encrypt_SHA256("qsec0fr"));
        csie.setUserGroups(new LinkedList<>());
        csie.setUuid(UUIDGenerator.generate(true));

        UserGroup adminGroup = new UserGroup();
        adminGroup.setName("Administrator");
        adminGroup.setDescription("This is the Administrator Group");
        adminGroup.setUserAccounts(new LinkedList<>());

        admin.getUserGroups().add(adminGroup);
        adminGroup.getUserAccounts().add(admin);
        csie.getUserGroups().add(adminGroup);
        adminGroup.getUserAccounts().add(csie);

        Professional vgay = new Professional();
        vgay.setEmail("vanessa.gay@gmail.com");
        vgay.setPassword(PasswordGenerator.encrypt_SHA256(PasswordGenerator.
                autoGenerate()));
        vgay.setUserGroups(new LinkedList<>());

        Professional skonx = new Professional();
        skonx.setEmail("skonx2006@hotmail.com");
        skonx.setPassword(PasswordGenerator.encrypt_SHA256(PasswordGenerator.
                autoGenerate()));
        skonx.setUserGroups(new LinkedList<>());

        Professional juju = new Professional();
        juju.setEmail("julien.sie@icloud.com");
        juju.setPassword(PasswordGenerator.encrypt_SHA256(PasswordGenerator.
                autoGenerate()));
        juju.setUserGroups(new LinkedList<>());

        Individual sgaysuard = new Individual();
        sgaysuard.setEmail("sylvie.gay.suard@gmail.com");
        sgaysuard.setUserGroups(new LinkedList<>());

        UserGroup pro = new UserGroup();
        pro.setName("Professional");
        pro.setDescription("This is the Professional User Group");
        pro.setUserAccounts(new LinkedList<>());

        UserGroup ind = new UserGroup();
        ind.setName("Individual");
        ind.setDescription("This is the Individual User Group");
        ind.setUserAccounts(new LinkedList<>());

        vgay.getUserGroups().add(pro);
        skonx.getUserGroups().add(pro);
        juju.getUserGroups().add(pro);

        pro.getUserAccounts().add(vgay);
        pro.getUserAccounts().add(skonx);
        pro.getUserAccounts().add(juju);

        em.persist(adminGroup);

        em.persist(pro);

        sgaysuard.getUserGroups().add(ind);
        ind.getUserAccounts().add(sgaysuard);

        em.persist(ind);

        sgaysuard.setPassword(PasswordGenerator.encrypt_SHA256("password"));
        sgaysuard.setUsername("Sylvioc");

        em.merge(sgaysuard);

        /*sylvioc.getUserGroups().remove(ind);
        ind.getUserAccounts().remove(sylvioc);
        em.remove(sylvioc);
        admin.getUserGroups().remove(adminGroup);
        adminGroup.getUserAccounts().remove(admin);
        em.remove(admin);*/
        initPaymentModes();
    }

    private void displayUserGroupDetails() {
        List<UserGroup> userGroup = userGroupFacade.findAll();
        userGroup.forEach(group -> {
            LOG.info("## GROUP ##");
            LOG.info("Name = " + group.getName());

            LOG.info("Description = " + group.getDescription());

            int n = group.getUserAccounts().size();
            LOG.info(n + " User" + (n > 1 ? "s" : ""));

            if (n > 0) {
                LOG.info("Users id: ");
            }
            group.getUserAccounts().forEach(u -> LOG.info("- "
                    + u.
                            getEmail()));

            LOG.info("###########");
        });
    }

    private void clean() {
        userGroupFacade.findAll().forEach(g -> em.remove(g));
        LOG.info("clean() : OK");
        LOG.info("EntityManager is"
                + (em.isJoinedToTransaction() ? "" : " not")
                + " joined to transaction");
        LOG.info("EntityManager is"
                + (em.isOpen() ? "" : " not") + " opened");
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
