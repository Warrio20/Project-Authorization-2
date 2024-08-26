package org.warrio.service;

import org.warrio.App;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

public class MailService {
    static Session session;
    static {
        String smtpHostServer = App.dotenv.get("SMTP_HOST");
        String smtpPort = App.dotenv.get("SMTP_PORT");
        String smtpUser = App.dotenv.get("MAIL_USER");
        String smtpPassword = App.dotenv.get("MAIL_PASSWORD");
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHostServer);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        };
        session = Session.getInstance(props, auth);
    }
    public static void sendActivation(String email, String link){
        try {
            String subject = "Email activation of account at " + App.dotenv.get("API_URL");
            String body = "<div><h1>To activate your account click to the link below</h1></div><div><a style=\"background-color: blue; border-radius: 0.5rem; text-decoration: none; text-align: center; padding: 8px; font-size: 2rem; color: white;\" href=" + link + ">Click here</a></div>";
            sendMail(email, subject, body);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    };
    public static void sendPasswordReset(String email, String link){
        try {
            String subject = "Reset password of account at " + App.dotenv.get("API_URL");
            String body = "<div><h1>Someone tried to reset password of your account at " + App.dotenv.get("API_URL") + ", if it's not you, don't do anything.</h1></div><div><a style=\"background-color: blue; border-radius: 0.5rem; text-decoration: none; text-align: center; padding: 8px; font-size: 2rem; color: white;\" href=" + link + ">Click here</a></div>";
            sendMail(email, subject, body);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void sendMail(String email, String subject, String body) throws MessagingException, UnsupportedEncodingException {
        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");
        msg.setFrom(new InternetAddress("no_reply@example.com", "NoReply"));
        msg.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
        msg.setSubject(subject, "UTF-8");

        msg.setContent(body, "text/html");

        msg.setSentDate(new Date());
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        Transport.send(msg);
    }
}
