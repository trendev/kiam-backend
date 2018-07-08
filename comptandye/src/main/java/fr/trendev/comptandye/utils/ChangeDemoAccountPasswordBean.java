/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import fr.trendev.comptandye.sessions.ProfessionalFacade;
import fr.trendev.comptandye.utils.observers.qualifiers.NewPasswordDemoAccount;
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
public class ChangeDemoAccountPasswordBean {

    @Inject
    private ProfessionalFacade professionalFacade;

    @Inject
    private PasswordGenerator passwordGenerator;

    @Inject
    @NewPasswordDemoAccount
    private Event<JsonObject> newPasswordEvent;

    private final Logger LOG;

    private final String DEMO_ACCOUNT_EMAIL;

    public ChangeDemoAccountPasswordBean() {
        this.DEMO_ACCOUNT_EMAIL = "pro@domain.com";
        this.LOG = Logger.getLogger(ChangeDemoAccountPasswordBean.class.
                getName());
    }

    @PostConstruct
    public void init() {
        LOG.info("ChangeDemoAccountPasswordBean initialized");
    }

    /**
     * Renew Demo Account password every Monday at 8 am and send the password on
     * Slack channel
     */
    @Schedules({
        @Schedule(dayOfWeek = "Mon", hour = "8"/*, minute = "30", second = "0"*/,
                persistent = false)
    //       ,
//        @Schedule(second = "*/20", minute = "*", hour = "*", persistent = false)
    })
    public void atSchedule() {
        try {
            Optional.ofNullable(professionalFacade.find(DEMO_ACCOUNT_EMAIL))
                    .ifPresent(pro -> {
                        String password = passwordGenerator.autoGenerate();

                        LOG.info("New Password generated for Demo Account "
                                + DEMO_ACCOUNT_EMAIL);

                        pro.setPassword(passwordGenerator.encrypt_SHA256(
                                password));

                        LOG.info("New encrypted password for Demo Account "
                                + DEMO_ACCOUNT_EMAIL + " has been saved");

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
                        "New password for user `"
                        + DEMO_ACCOUNT_EMAIL + "`")
                .add("text", "*" + password + "*")
                .build();
    }

}
