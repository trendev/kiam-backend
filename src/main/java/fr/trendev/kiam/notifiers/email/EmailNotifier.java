/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.kiam.notifiers.email;

import fr.trendev.kiam.useraccount.entities.NewProfessionalCreated;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.event.ObservesAsync;
import javax.json.JsonObject;
import javax.mail.MailSessionDefinition;
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
@MailSessionDefinition( // Jakarta Mail
        name = "java:app/kiam/google-gsuite",
        description = "JavaMail session based on Google Suite @kiam.fr",
        from = "support@kiam.fr",
        host = "smtp.gmail.com",
        // TODO : set in K8S secrets+ENV "kmpnfpoojsqtjibn"
        //password = "${ENV=GOOGLE_KIAM_PASSWORD}",
        password = "kmpnfpoojsqtjibn",
        user = "no-reply@kiam.fr",
        transportProtocol = "smtp",
        storeProtocol = "imap",
        properties = {
            "mail.debug= false",
            "mail.from=support@kiam.fr",
            "mail-auth=true",
            "mail.smtp.auth=true",
            "mail.smtp.port=465",
            "mail.smtp.socketFactory.port=465",
            "mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory"
        }
)
@Singleton
@Startup
public class EmailNotifier {

    @Resource(name = "java:app/kiam/google-gsuite")
    Session mailSession;

    private static final Logger LOG = Logger.getLogger(EmailNotifier.class.getName());

    @PostConstruct
    public void init() {
        LOG.info("Email Notifier is UP and RUNNING ");
    }

    @PreDestroy
    public void close() {
        LOG.info("Email Notifier is CLOSING");
    }

    private void sendAutogeneratedPassword(String email, String pwd) {
        try {
            MimeMessage message = new MimeMessage(mailSession);

            message.setSubject("\u2728 Inscription réussie \u2728");
            message.setFrom(new InternetAddress("support@kiam.fr", "kiam"));
            message.setReplyTo(new InternetAddress[]{
                new InternetAddress("support@kiam.fr", "kiam")
            });
            message.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(email));
            message.setDescription("Compte créé");
            message.setContent("<!DOCTYPE html>\n"
                    + "<html>\n"
                    + "<body>\n"
                    + "<h2>Merci pour votre inscription et bienvenue sur kiam !!!</h2>\n"
                    + "<p><b>Afin de générer vos premières factures, vous devrez tout d'abord : </b></p>"
                    + "<ol>"
                    + "<li>"
                    + "vous connecter sur l'application"
                    + "</li>"
                    + "<li>"
                    + "compléter votre profil"
                    + "</li>"
                    + "<li>"
                    + "générer vos premières offres"
                    + "</li>"
                    + "<li>"
                    + "créer votre première fiche client"
                    + "</li>"
                    + "<li>"
                    + "ouvrir la fiche client"
                    + "</li>"
                    + "<li>"
                    + "cliquer sur l'icone '+'"
                    + "</li>"
                    + "</ol>"
                    + "<div>\n"
                    + "<h3>Voici les identifiants que nous avons automatiquement créés pour vous \uD83D\uDE48</h3>\n"
                    + "<table style=\"width:100%;\">\n"
                    + "  <tr>\n"
                    + "    <td>Identifiant / Email : </td>\n"
                    + "    <td><code>" + email + "</code></td>\n"
                    + "  </tr>\n"
                    + "  <tr>\n"
                    + "    <td>Mot de passe : </td>\n"
                    + "    <td><code>" + pwd + "</code></td>\n"
                    + "  </tr>\n"
                    + "</table>"
                    + "</div>\n"
                    + "<p><b><i>Attention aux majuscules et aux miniscules dans le mot de passe...</i></b></p>"
                    + "<p>Vous pouvez d'ores et déjà les utiliser sur <a href=\"https://kiam.fr/app/\">l'appli</a> \uD83D\uDE80</p>\n"
                    + "<p>Si vous rencontrez des difficultés, n'hésitez surtout pas à <a href=\"mailto:support@kiam.fr\">contacter le support</a>"
                    + " et à visiter la <a href=\"https://kiam.fr/support\">FAQ</a> \uD83D\uDE09</p>"
                    + "</body>\n"
                    + "\n"
                    + "</html>",
                    "text/html; charset=utf-8");

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, "Error sending autogenerated password [" + pwd + "]", ex);
        }
    }

    public void observeNewProfessionalCreated(
            @ObservesAsync @NewProfessionalCreated JsonObject entity) {

        String email = entity.getString("email");
        String password = entity.getString("password");

        this.sendAutogeneratedPassword(email, password);
    }
}
