/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.trendev.comptandye.beans;

import java.io.UnsupportedEncodingException;
import javax.annotation.Resource;
import javax.ejb.Startup;
import javax.inject.Singleton;
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
public class EmailSender {

    @Resource(name = "java/mail/comptandye")
    Session mailSession;

    public void sendEmail(String toEmail, String msg) throws MessagingException,
            UnsupportedEncodingException {
        Message message = new MimeMessage(mailSession);

        message.setSubject("Hi");
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(
                toEmail, "Random Guest"));
        message.setContent("<p>" + msg + "</p>",
                "text/html; charset=utf-8");

        Transport.send(message);
    }
}
