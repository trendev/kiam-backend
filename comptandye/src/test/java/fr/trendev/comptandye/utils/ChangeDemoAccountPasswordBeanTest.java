/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import org.junit.Test;

/**
 *
 * @author jsie
 */
public class ChangeDemoAccountPasswordBeanTest {

    private final String DEMO_ACCOUNT_EMAIL;

    private final String SLACK_URL;

    private final String TOKEN;

    private final Client client;

    public ChangeDemoAccountPasswordBeanTest() {
        this.DEMO_ACCOUNT_EMAIL = "pro@domain.com";
        this.SLACK_URL = "https://slack.com/api/chat.postMessage";
        this.TOKEN = "xoxa-320251608305-395708530182-394370785636-d227cf997e97f4d4b650e4ed31d48434";
        this.client = ClientBuilder.newClient();
    }

    @Test
    public void testSlack() throws Exception {
        client.target(SLACK_URL)
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + TOKEN);
    }

}
