/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.security.controllers;

import fr.trendev.comptandye.professional.controllers.ProfessionalFacade;
import fr.trendev.comptandye.security.controllers.qualifiers.NewDemoAccountPassword;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author jsie
 */
@Singleton
@Startup
public class NewDemoAccountPasswordScheduler {

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    private PasswordManager passwordManager;

    @Inject
    @NewDemoAccountPassword
    private Event<JsonObject> newPasswordEvent;

    private static final Logger LOG = Logger.getLogger(
            NewDemoAccountPasswordScheduler.class.
                    getName());

    private final String DEMO_ACCOUNT_EMAIL;

    public NewDemoAccountPasswordScheduler() {
        this.DEMO_ACCOUNT_EMAIL = "pro@domain.com";
    }

    @PostConstruct
    public void init() {
        LOG.info("NewDemoAccountPasswordScheduler initialized");
    }

    /**
     * Renew Demo Account password every Monday at 8:30 am and send the password
     * on Slack channel
     */
    @Schedules({
        @Schedule(dayOfWeek = "Mon", hour = "8", minute = "30"/*, second = "0"*/,
                persistent = false)
    //       ,
//        @Schedule(second = "*/20", minute = "*", hour = "*", persistent = false)
    })
    public void atSchedule() {
        try {
            Optional.ofNullable(professionalFacade.find(DEMO_ACCOUNT_EMAIL))
                    .ifPresent(pro -> {
                        String password = passwordManager.autoGenerate();

                        LOG.log(Level.INFO,
                                "New Password generated for Demo Account {0}",
                                DEMO_ACCOUNT_EMAIL);

                        pro.setPassword(passwordManager.hashPassword(
                                password));

                        LOG.log(Level.INFO,
                                "New encrypted password for Demo Account {0} has been saved",
                                DEMO_ACCOUNT_EMAIL);

                        newPasswordEvent.fire(buildText(password));

                    });

        } catch (Exception ex) {
            LOG.log(Level.SEVERE,
                    "Error occurs during demo account password renewal",
                    ex);
        }

    }

    /**
     * Creates the content of the slack message with the new password
     *
     * @param password the new password
     * @return an object which will be embedded in the slack message
     */
    private JsonObject buildText(String password) {
        return Json.createObjectBuilder()
                .add("pretext",
                        "_A new password has been set for the demo account_")
                .add("color", "#673ab7")
                .add("text", "*" + password + "*")
                .add("footer",
                        "this is the new password for `" + DEMO_ACCOUNT_EMAIL
                        + "`")
                .build();
    }

}
