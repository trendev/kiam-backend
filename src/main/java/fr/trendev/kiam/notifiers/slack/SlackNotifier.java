/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.notifiers.slack;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.kiam.security.controllers.qualifiers.FirestoreIssue;
import fr.trendev.kiam.security.controllers.qualifiers.JWTForgeryDetected;
import fr.trendev.kiam.security.controllers.qualifiers.LoginDetected;
import fr.trendev.kiam.security.controllers.qualifiers.LogoutDetected;
import fr.trendev.kiam.security.controllers.qualifiers.NewDemoAccountPassword;
import fr.trendev.kiam.useraccount.entities.NewProfessionalCreated;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.Observes;
import javax.enterprise.event.ObservesAsync;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 *
 * @author jsie
 */
@Singleton
@Startup
public class SlackNotifier {

    private final String SLACK_URL;
    private final String AUTHENTICATION_CHANNEL;
    private final String FIRESTORE_CHANNEL;
    private final String LOGINS_CHANNEL;
    private final String NEW_PROFESSIONAL_CHANNEL;

    @Inject
    @ConfigProperty(name = "SLACK_KIAM_TOKEN")
    private String token;

    private final Client client;
    private static final Logger LOG = Logger.getLogger(SlackNotifier.class.getName());

    @Inject
    ObjectMapper om;

    @Inject
    @ConfigProperty(name = "MY_POD_NAMESPACE", defaultValue = "dev") // should be ENV var
    private String namespace;

    public SlackNotifier() {
        this.SLACK_URL = "https://slack.com/api/chat.postMessage";
        this.AUTHENTICATION_CHANNEL = "GB1R67HL2"; // slack channel: "authentication"
        this.LOGINS_CHANNEL = "GC0K0E00P"; // slack channel : "logins"
        this.FIRESTORE_CHANNEL = "GF6D78C6A"; // slack channel : "firestore"
        this.NEW_PROFESSIONAL_CHANNEL = "GMFLQ53PF"; // slack channel : "new-password"
        this.client = ClientBuilder.newClient();
    }

    @PostConstruct
    public void init() {
        LOG.log(Level.INFO, "SlackServiceObserver initialized for Environment [{0}]", this.namespace);
        LOG.log(Level.INFO, "SLACK_KIAM_TOKEN = {0}",token);
    }

    /**
     * Observes if a new password is created for the demo account and send it to
     * slack channel (authentication)
     *
     * @param jo the content of the slack message
     */
    public void observeDemoAccountNewPassword(
            @Observes(during = TransactionPhase.AFTER_SUCCESS)
            @NewDemoAccountPassword JsonObject jo) {
        this.controlPostMessage(jo, AUTHENTICATION_CHANNEL);
    }

    public void observeLogins(@Observes @LoginDetected JsonObject jo) {
        this.controlPostMessage(jo, LOGINS_CHANNEL);
    }

    public void observeLogouts(@Observes @LogoutDetected JsonObject jo) {
        this.controlPostMessage(jo, LOGINS_CHANNEL);
    }

    public void observeFirestoreIssues(
            @Observes @FirestoreIssue JsonObject jo) {
        this.controlPostMessage(jo, FIRESTORE_CHANNEL);
    }

    public void observeJWTForgery(
            @Observes @JWTForgeryDetected JsonObject jo) {
        this.controlPostMessage(jo, AUTHENTICATION_CHANNEL);
    }

    public void observeNewProfessionalCreated(
            @ObservesAsync @NewProfessionalCreated JsonObject entity) {

        String email = entity.getString("email");
        String password = entity.getString("password");

        JsonObjectBuilder builder = Json.createObjectBuilder()
                .add("pretext", "*Professional Account created*")
                .add("text", "email : " + email)
                .add("footer", "password : " + password)
                .add("color", "#673ab7");

        JsonObject jo = builder.build();

        this.controlPostMessage(jo, NEW_PROFESSIONAL_CHANNEL);

    }

    private void controlPostMessage(JsonObject object, final String channel) {
        try {
            Response response = postMessage(buildPostMessage(object, channel));

            if (response.getStatus() != 200) {
                throw new IllegalStateException(
                        "Slack message error. Status = " + response.getStatus());
            }

            // controls if the request is ok
            if (response.hasEntity()) {
                String output = response.readEntity(String.class);
                JsonNode node = om.readTree(output).get("ok");
                if (!node.isBoolean()) {
                    throw new IllegalStateException(
                            "Slack message error. No field \"ok\".\n" + output);
                }
                if (!node.asBoolean()) {
                    throw new IllegalStateException(
                            "Slack message error : \"ok\" field is false !\n"
                            + output);
                }

            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE,
                    "Error occurs posting a message in Slack in channel "
                    + channel,
                    ex);
        }
    }

    /**
     * Sends the message in the slack channel
     *
     * @param object the slack message
     * @return the response (usually HTTP 200 OK)
     */
    public Response postMessage(JsonObject object) {
        return client.target(SLACK_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .post(Entity.entity(object, MediaType.APPLICATION_JSON));
    }

    /**
     * Builds the content of the slack message from the observed event
     *
     * @param object the observed event
     * @return the slack message
     */
    private JsonObject buildPostMessage(JsonObject object, final String channel) {
        return Json.createObjectBuilder()
                .add("channel", channel)
                .add("text", "Environment : *" + namespace + "*")
                .add("attachments", Json.createArrayBuilder()
                        .add(object))
                .build();
    }

}
