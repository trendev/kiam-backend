/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.notifiers.email;

import fr.trendev.comptandye.useraccount.entities.NewProfessionalCreated;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.ObservesAsync;
import javax.json.JsonObject;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author jsie
 */
@Singleton
@Startup
public class EmailNotifier {

    @Resource(name = "java/mail/google-comptandye")
    Session mailSession;

    private static final Logger LOG = Logger.getLogger(EmailNotifier.class.getName());

    private void sendAutogeneratedPassword(String email, String pwd) {
        try {
            MimeMessage message = new MimeMessage(mailSession);

            message.setSubject("\u2728 Inscription réussie\u2728");
            message.setFrom(new InternetAddress("support@comptandye.fr", "comptandye"));
            message.setReplyTo(new InternetAddress[]{
                new InternetAddress("support@comptandye.fr", "comptandye")
            });
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(email));
            message.setDescription("Account created");
            message.setContent("<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<body>\n"
                    + "<h2>Merci pour ton inscription et bienvenue sur comptandye !!!</h2>\n"
                    + "<div>\n"
                    + "<h3>Voici les identifiants que nous avons automatiquement créés pour toi \uD83D\uDE48</h3>\n"
                    + "<p>Email : <code>" + email + "</code></p>\n"
                    + "<p>Mot de passe : <code> " + pwd + "</code></p>\n"
                    + "</div>\n"
                    + "<div>\n"
                    + "<span>Tu peux d'ores et déjà les utiliser sur <a href=\"https://www.comptandye.fr\">l'appli</a> \uD83D\uDE80</span>\n"
                    + "</div>\n"
                    + "<span>Si tu rencontres des difficultés, n'hésite surtout pas à <a href=\"mailto:support@comptandye.fr\">nous contacter</a> \uD83D\uDE09</span>"
                    + "</body>\n"
                    + "\n"
                    + "</html>",
                    "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Error sending autogenerated password [" + pwd + "]", ex);
        }
    }

    public void onFireNewProfessionalCreated(
            @ObservesAsync @NewProfessionalCreated JsonObject entity) {

        String email = entity.getString("email");
        String password = entity.getString("password");

        this.sendAutogeneratedPassword(email, password);
    }
}
