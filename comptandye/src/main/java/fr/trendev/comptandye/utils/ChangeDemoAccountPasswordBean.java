/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.sessions.ProfessionalFacade;
import java.util.Optional;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author jsie
 */
@Singleton
@Startup
public class ChangeDemoAccountPasswordBean {

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    private PasswordGenerator passwordGenerator;

    private final Logger LOG;

    private final String DEMO_ACCOUNT_EMAIL;

    private final String SLACK_URL;

    private final String TOKEN;

    private final Client client;

    public ChangeDemoAccountPasswordBean() {
        this.DEMO_ACCOUNT_EMAIL = "pro@domain.com";
        this.SLACK_URL = "https://slack.com/api/chat.postMessage";
        this.TOKEN = "xoxa-320251608305-395708530182-394370785636-d227cf997e97f4d4b650e4ed31d48434";
        this.client = ClientBuilder.newClient();
        this.LOG = Logger.getLogger(ChangeDemoAccountPasswordBean.class.
                getName());
    }

    @PostConstruct
    public void init() {
        LOG.info("ChangeDemoAccountPasswordBean initialized");
    }

    @Schedule(second = "*/10", minute = "*", hour = "*", persistent = false)
    public void atSchedule() {

        Optional.ofNullable(professionalFacade.find(DEMO_ACCOUNT_EMAIL))
                .ifPresent(pro -> {
                    String password = passwordGenerator.autoGenerate();

                    LOG.info("New Password generated for Demo Account "
                            + DEMO_ACCOUNT_EMAIL);

                    pro.setPassword(passwordGenerator.encrypt_SHA256(password));

                });

        LOG.info("New encrypted password for Demo Account "
                + DEMO_ACCOUNT_EMAIL + " has been saved");
    }

}
