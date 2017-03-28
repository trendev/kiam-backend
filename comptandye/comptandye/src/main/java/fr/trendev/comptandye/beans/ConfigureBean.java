/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import fr.trendev.comptandye.ejbsessions.UserGroupFacade;
import fr.trendev.comptandye.entities.Administrator;
import fr.trendev.comptandye.entities.Individual;
import fr.trendev.comptandye.entities.Professional;
import fr.trendev.comptandye.entities.UserGroup;
import fr.trendev.comptandye.util.PasswordGenerator;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
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

    @PersistenceContext(unitName = "DEFAULT_PU")
    private EntityManager em;

    @EJB
    UserGroupFacade userGroupFacade;

    @PostConstruct
    public void init() {

        testCreateUsersAndGroups();

        testDisplayUserGroupDetails();
    }

    private void testCreateUsersAndGroups() {
        Administrator admin = new Administrator();
        admin.setEmail("julien.sie@gmail.com");
        admin.setPassword(PasswordGenerator.encrypt_SHA256("password"));
        admin.setUserGroups(new LinkedList<>());

        UserGroup adminGroup = new UserGroup();
        adminGroup.setName("Administrator");
        adminGroup.setDescription("This is the Administrator Group");
        adminGroup.setUserAccounts(new LinkedList<>());

        admin.getUserGroups().add(adminGroup);

        adminGroup.getUserAccounts().add(admin);

        Professional vgay = new Professional();
        vgay.setEmail("vanessa.gay@gmail.com");
        vgay.setPassword(PasswordGenerator.autoGenerate());
        vgay.setUserGroups(new LinkedList<>());

        Professional skonx = new Professional();
        skonx.setEmail("skonx2006@hotmail.com");
        skonx.setPassword(PasswordGenerator.autoGenerate());
        skonx.setUserGroups(new LinkedList<>());

        Professional juju = new Professional();
        juju.setEmail("julien.sie@icloud.com");
        juju.setPassword(PasswordGenerator.autoGenerate());
        juju.setUserGroups(new LinkedList<>());

        Individual sylvioc = new Individual();
        sylvioc.setEmail("sylvie.gay.suard@gmail.com");
        sylvioc.setUserGroups(new LinkedList<>());

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

        em.persist(admin);
        em.persist(adminGroup);

        em.persist(vgay);
        em.persist(skonx);
        em.persist(juju);
        em.persist(pro);

        sylvioc.getUserGroups().add(ind);
        ind.getUserAccounts().add(sylvioc);

        em.persist(sylvioc);
        em.persist(ind);

        sylvioc.setPassword(PasswordGenerator.encrypt_SHA256("password"));
        em.merge(sylvioc);
    }

    private void testDisplayUserGroupDetails() {
        List<UserGroup> userGroup = userGroupFacade.findAll();

        userGroup.forEach(group -> {
            System.out.println("## GROUP ##");
            System.out.println("Name = " + group.getName());
            System.out.println("Description = " + group.getDescription());
            int n = group.getUserAccounts().size();
            System.out.println(n + " User" + (n > 1 ? "s" : ""));
            System.out.println("Users id: ");
            group.getUserAccounts().forEach(u -> System.out.println("- " + u.
                    getEmail()));
            System.out.println("###########");
        });
    }

}
