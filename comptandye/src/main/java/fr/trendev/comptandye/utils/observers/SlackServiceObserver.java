/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils.observers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.trendev.comptandye.utils.observers.qualifiers.NewPasswordDemoAccount;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author jsie
 */
@Singleton
public class SlackServiceObserver {

    private final String SLACK_URL;
    private final String CHANNEL;
    private final String TOKEN;

    private final Client client;
    private final Logger LOG;

    @Inject
    ObjectMapper om;

    public SlackServiceObserver() {
        this.SLACK_URL = "https://slack.com/api/chat.postMessage";
        this.CHANNEL = "GB1R67HL2"; // slack channel: "authentication"
        this.TOKEN = "xoxa-320251608305-395708530182-394370785636-d227cf997e97f4d4b650e4ed31d48434";
        this.client = ClientBuilder.newClient();
        this.LOG = Logger.getLogger(SlackServiceObserver.class.getName());
    }

    @PostConstruct
    public void init() {
        LOG.info("SlackServiceObserver initialized");
    }

    public void observeDemoAccountNewPassword(
            @Observes(during = TransactionPhase.AFTER_SUCCESS)
            @NewPasswordDemoAccount JsonObject object) {
        try {
            Response response = postMessage(buildPostMessage(object));
            if (response.getStatus() != 200) {
                throw new IllegalStateException(
                        "Slack message error. Status = " + response.getStatus());
            }
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
                    "Error occurs posting a message in Slack (new password demo account notification)",
                    ex);
        }

    }

    public Response postMessage(JsonObject object) {
        return client.target(SLACK_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN)
                .post(Entity.entity(object, MediaType.APPLICATION_JSON));
    }

    private JsonObject buildPostMessage(JsonObject object) {
        return Json.createObjectBuilder()
                .add("channel", CHANNEL)
                .add("attachments", Json.createArrayBuilder()
                        .add(object))
                .build();
    }

}
